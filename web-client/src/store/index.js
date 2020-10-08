import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import getters from "./getters";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    id: null,
    name: null,
    role: null,
    jwt: '',
    isLogin: false,
    echoJwt: '',
    color: null,
    avatar: null,
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
      state.color = payload.color;
      state.avatar = payload.avatar;
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
      state.color = null;
      state.avatar = null;
    },
    saveName: (state, payload) => {
      state.name = payload.name;
    },
    saveColor: (state, payload) => {
      state.color = payload.color;
    },
    saveAvatar: (state, payload) => {
      state.avatar = payload.avatar;
    }
  },
  modules: {
    app
  },
  getters
})
