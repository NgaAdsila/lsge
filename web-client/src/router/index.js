import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue'
import BaseLayout from '../layouts/default.vue'
import store from '../store/index'
import ApiService from '../helper/ApiService'
import { API_PATH } from '../services/constants'
import { checkRole } from '../services/role'
import Home from '../pages/Home'
import LoginHistory from '../pages/login-history/index';
import Profile from '../pages/profile/index';
import ForgetPassword from '../pages/forget-password/index';
import AboutMe from "../pages/about-me/index";
import ContactUs from '../pages/contact-us/index';
import OldBookStoreHome from '../pages/book-store/old/index';
import OldBookStoreAdmin from '../pages/book-store/old/admin';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    component: BaseLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: Home
      },
      {
        path: '/login-history',
        name: 'Login history',
        component: LoginHistory
      },
      {
        path: '/profile',
        name: 'Profile',
        component: Profile
      },
      {
        path: '/about-me',
        name: 'AboutMe',
        component: AboutMe
      },
      {
        path: '/contact-us',
        name: 'ContactUs',
        component: ContactUs
      },
      {
        path: '/book-store/old-home',
        name: 'OldBookStoreHome',
        component: OldBookStoreHome,
      },
      {
        path: '/book-store/old-admin',
        name: 'OldBookStoreAdmin',
        component: OldBookStoreAdmin,
      },
      {
        path: '/',
        redirect: '/home'
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/forget-password',
    name: 'ForgetPassword',
    component: ForgetPassword
  },
  {
    path: '*',
    redirect: '/'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes
});

router.beforeEach((to, from, next) => {
  store.commit('initialiseStore');
  if (to.matched.some(r => r.meta.requiresAuth) && !store.getters.isLogin) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
  if (store.getters.isLogin && ['/forget-password', '/login', '/register'].some(t => t === to.path)) {
    next({ path: '/' });
  } else if (store.getters.isLogin && to.path !== '/login') {
    ApiService.getAxios().get(API_PATH.USER_ROLE)
        .then(function (res) {
          if (!checkRole(to.path, res.data)) {
            next({ path: '/' })
          }
        })
        .catch(function (e) {
          console.log('Login error: ', e);
        })
  }
});

export default router
