import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    id: null,
    name: null,
    role: null,
    jwt: '',
    isLogin: false,
    echoJwt: ''
  },
  getters: {
    isLogin: state => state.isLogin,
    jwt: state => state.jwt,
    role: state => state.role,
    id: state => state.id,
    name: state => state.name,
    echoJwt: state => state.echoJwt
  },
  mutations: {
    initialiseStore(state) {
      if (localStorage.getItem('store')) {
        this.replaceState(Object.assign(state, JSON.parse(localStorage.getItem('store'))));
      }
    },
    doLogin: (state, payload) => {
      state.role = payload.role;
      state.isLogin = true;
      state.userId = payload.userId;
      state.jwt = payload.jwt;
      state.name = payload.name;
      state.echoJwt = payload.echoJwt;
    },
    saveRole: (state, payload) => {
      state.role = payload.role;
    },
    saveJwt: (state, payload) => {
      state.jwt = payload;
    },
    reset: (state) => {
      state.role = null;
      state.isLogin = false;
      state.jwt = '';
      state.userId = null;
      state.name = null;
      state.echoJwt = null;
    },
    saveName: (state, payload) => {
      state.name = payload.name;
    }
  }
})
