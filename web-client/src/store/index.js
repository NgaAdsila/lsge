import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLogin: false,
    jwt: '',
    userName: '',
    role: '',
    userId: null
  },
  getters: {
    isLogin: state => state.isLogin,
    jwt: state => state.jwt,
    userName: state => state.userName,
    role: state => state.role,
    userId: state => state.userId
  },
  mutations: {
    doLogin: (state, payload) => {
      state.userName = payload.name;
      state.role = payload.role;
      state.isLogin = true;
      state.userId = payload.userId;
    },
    doLogout: (state) => {
      state.isLogin = false;
    },
    saveRole: (state, payload) => {
      state.role = payload.role;
    },
    saveJwt: (state, payload) => {
      state.jwt = payload;
    },
    reset: (state) => {
      state.userName = '';
      state.role = '';
      state.isLogin = false;
      state.jwt = '';
      state.userId = null;
    }
  }
})
