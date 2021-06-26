package com.example.coursesystem.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.coursesystem.common.dto.FileDto;
import com.example.coursesystem.common.lang.Result;
import com.example.coursesystem.entity.Filedata;
import com.example.coursesystem.entity.Student;
import com.example.coursesystem.entity.Teacher;
import com.example.coursesystem.service.FiledataService;
import com.example.coursesystem.service.TeacherService;
import com.sun.org.apache.regexp.internal.RE;
import net.sf.saxon.lib.Resource;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yz
 * @since 2021-06-17
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController extends BaseController{
    @Autowired
    TeacherService teacherService;
    @Autowired
    FiledataService filedataService;
    @CrossOrigin
    @GetMapping("/list")
    public Result list(String tno,Integer current,Integer size) {
        if (current!=null&size!=null){
            IPage page= new Page<>(current,size);
            Page<Teacher> pageData = (Page<Teacher>) teacherService.page(page,new QueryWrapper<Teacher>().like(StrUtil.isNotBlank(tno),"tno",tno).or().like(StrUtil.isNotBlank(tno),"tname",tno));
            return Result.success(pageData);
        }
        Page<Teacher> pageData = (Page<Teacher>) teacherService.page(getPage(),
                new QueryWrapper<Teacher>()
                        .like(StrUtil.isNotBlank(tno), "sno", tno)
        );
        return Result.success(pageData);
    }
    //教师信息获取
    @GetMapping("/Info/{id}")
    public Result getStuInfo(@PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        Assert.notNull(teacher, "找不到该教师！");
        return Result.success(teacher);
    }
    //教师信息更新
    @CrossOrigin
    @PostMapping("/update")
    public Result update(@Validated @RequestBody Teacher teacher) {
        if (teacher.getSex().equals("1")){
            teacher.setSex("女");
        }
        else {
            teacher.setSex("男");
        }
        if (StrUtil.isNotBlank(teacher.getPwd())) {
            teacherService.updateById(teacher);
        }
        return Result.success(teacher);
    }
    //教师信息保存
    @CrossOrigin
    @PostMapping("/save")
    public Result save(@Validated @RequestBody Teacher teacher) {
        if (teacher.getSex().equals("1")){
            teacher.setSex("女");
        }
        else {
            teacher.setSex("男");
        }
        if (StrUtil.isNotBlank(teacher.getPwd())) {
            teacherService.save(teacher);
        }
        return Result.success(teacher);
    }

    //教师删除
    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        teacherService.removeByIds(Arrays.asList(ids));
        teacherService.remove(new QueryWrapper<Teacher>().in("id", ids));
        return Result.success("操作成功");
    }


    private  final static String rootPath = "D:/downloadTest/";
    @RequestMapping("/file/upload")
    public Object uploadFile(MultipartFile[] multipartFiles,String cno){
        File fileDir = new File(rootPath);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for(int i = 0;i<multipartFiles.length;i++){
                    try {
                        //以原来的名称命名,覆盖掉旧的，这里也可以使用UUID之类的方式命名，这里就没有处理了
                        String storagePath = rootPath+multipartFiles[i].getOriginalFilename();
                        System.out.println("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                +"，保存的路径为：" + storagePath);
                        // 3种方法： 第1种
//                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);
                        // 第2种
//                        Path path = Paths.get(storagePath);
//                        Files.write(path,multipartFiles[i].getBytes());
                        // 第3种
                        Filedata filedata = new Filedata();
                        filedata.setFilename(storagePath.substring(16));
                        filedata.setFilepath(storagePath);
                        filedata.setCno(cno.substring(8,cno.length()-2));
                        filedataService.save(filedata);
                        multipartFiles[i].transferTo(new File(storagePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //前端可以通过状态码，判断文件是否上传成功
        return Result.success("文件上传成功");
    }
    @CrossOrigin
    @GetMapping("/file/getFileList")
    public Result fileList(String cno) {
        cno = cno.substring(8,cno.length()-2);
        Page<Filedata> pageData = (Page<Filedata>) filedataService.page(getPage(),
                new QueryWrapper<Filedata>()
                        .eq(StrUtil.isNotBlank(cno), "cno", cno)
        );
        return Result.success(pageData);
    }
    /**
     *
     * @param fileName 文件名
     * @param response
     * @return
     */
    @CrossOrigin
    /**
     *
     * @param fileName 文件名
     * @param response
     * @return
     */
    @RequestMapping("/file/download")
    public Object downloadFile(@RequestParam String fileName, HttpServletResponse response){
        OutputStream os = null;
        InputStream is= null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/x-download;charset=utf-8");
            //Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，
            // 保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
            //把文件名按UTF-8取出，并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"),"ISO8859-1"));
            //读取流
            File f = new File(rootPath+fileName);
            is = new FileInputStream(f);
            if (is == null) {
                System.out.println("下载附件失败，请检查文件“" + fileName + "”是否存在");
                return Result.fail("下载附件失败，请检查文件“" + fileName + "”是否存在");
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            return Result.fail("下载附件失败,error:"+e.getMessage());
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //其实，这个返回什么都不重要
        return Result.success("下载成功");
    }


}
