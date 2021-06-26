import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/login.vue'
import adminManageUser from '../views/admin/adminManageUser.vue'
import Home from '../views/Home.vue'
import teacherManage from '../views/admin/teacherManage'
import newsList from '../views/admin/newsList'
import newsEdit from '../views/admin/newsEdit'
import StuHome from '../views/StuHome'
import NewsDetail from '../views/news/NewsDetail'
import TeacherHome from "@/views/TeacherHome";
import TeachernewsDetail from "@/views/news/TeachernewsDetail";
import TeacherIndex from "@/views/teacher/TeacherIndex";
import CourseList from "@/views/admin/CourseList";
import StudentIndex from "@/views/student/StudentIndex";
import TeacherCourse from "@/views/teacher/TeacherCourse";
import CourseDetail from "@/views/teacher/CourseDetail";
import StudentInfo from "@/views/student/StudentInfo";
import TeacherInfo from "@/views/teacher/TeacherInfo";
import StuCourseDetail from "@/views/student/StuCourseDetail";
import StuCourse from "@/views/student/StuCourse";
Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },

    //管理员模块
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      requireAuth: true
    },
    children: [
      {
        path: '/admin/stuManage',
        name: 'adminManageUser',
        component: adminManageUser
      },
      {
        path: '/admin/teacherManage',
        name: 'teacherManage',
        component: teacherManage
      },
      {
        path: '/admin/newsList',
        name: 'newsList',
        component: newsList,
      },
      {
        path: '/admin/newsEdit',
        name: 'newsEdit',
        component: newsEdit
      },
      {
        path: '/admin/courseList',
        name: 'CourseList',
        component: CourseList
      },
    ]
  },
  //学生模块
  {
    path: '/',
    name: 'StuHome',
    component: StuHome,
    meta: {
      requireAuth: true
    },
    children: [
      {
        path: '/student/index',
        name: 'StudentIndex',
        component: StudentIndex
      },
      {
        path: '/student/newsDetail',
        name: 'NewsDetail',
        component: NewsDetail
      },
      {
        path: '/student/info',
        name: 'StudentInfo',
        component: StudentInfo
      },
      {
        path: '/student/course',
        name: 'StuCourse',
        component: StuCourse
      },
      {
        path: '/student/course/:cno',
        name: 'StuCourseDetail',
        component: StuCourseDetail
      },
    ]
  },
  //教师模块
  {
    path: '/',
    name: 'TeacherHome',
    component: TeacherHome,
    meta: {
      requireAuth: true
    },
    children: [
      {
        path: '/teacher/index',
        name: 'TeacherIndex',
        component: TeacherIndex
      },
      {
        path: '/teacher/TeachernewsDetail',
        name: 'TeachernewsDetail',
        component: TeachernewsDetail
      },
      {
        path: '/teacher/info',
        name: 'TeacherInfo',
        component: TeacherInfo
      },
      {
        path: '/teacher/course',
        name: 'TeacherCourse',
        component: TeacherCourse
      },
      {
        path: '/teacher/course/:cno',
        name: 'CourseDetail',
        component: CourseDetail
      },
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
