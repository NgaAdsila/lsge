import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue'
import BaseLayout from '../layouts/default.vue'
import store from '../store/index'
import ApiService from '../helper/ApiService'
import { API_PATH } from '../services/constants'
import { checkRole } from '../services/role'
import Home from '../components/HelloWorld'

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
  if (to.matched.some(r => r.meta.requiresAuth) && !store.getters.isLogin) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
  if (store.getters.isLogin && to.path !== '/login') {
    ApiService.getAxios().get(API_PATH.USER_ROLE)
      .then(function (res) {
        if (store.getters.isLogin && to.path !== '/login' && checkRole(to.path, res.data)) {
          next({ path: '/' })
        }
      })
      .catch(function (e) {
        console.log('Login error: ', e);
      })
  }
});

export default router
