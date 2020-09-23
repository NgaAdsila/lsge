import Vue from 'vue'
import VueRouter from 'vue-router'
import BaseLayout from '../layouts/default.vue'
import store from '../store/index'
import ApiService from '../helper/ApiService'
import { API_PATH } from '../services/constants'
import { checkRole } from '../services/role'

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
        component: () => import(/* webpackChunkName: "home" */ '../pages/Home')
      },
      {
        path: '/login-history',
        name: 'Login history',
        component: () => import(/* webpackChunkName: "login-history" */ '../pages/login-history/index')
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import(/* webpackChunkName: "profile" */ '../pages/profile/index')
      },
      {
        path: '/about-me',
        name: 'AboutMe',
        component: () => import(/* webpackChunkName: "about-me" */ '../pages/about-me/index')
      },
      {
        path: '/contact-us',
        name: 'ContactUs',
        component: () => import(/* webpackChunkName: "contact-us" */ '../pages/contact-us/index')
      },
      {
        path: '/chat-list',
        name: 'ChatList',
        component: () => import(/* webpackChunkName: "chat-list" */ '../pages/chat/index')
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
    component: () => import(/* webpackChunkName: "login" */ '../pages/Login')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import(/* webpackChunkName: "register" */ '../pages/Register')
  },
  {
    path: '/forget-password',
    name: 'ForgetPassword',
    component: () => import(/* webpackChunkName: "forget-password" */ '../pages/forget-password')
  },
  {
    path: '*',
    redirect: '/'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ x: 0, y: 0 }),
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
