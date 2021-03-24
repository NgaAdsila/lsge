import '@babel/polyfill';
import 'mutationobserver-shim';
import Vue from 'vue';
import './plugins/bootstrap-vue';
import App from './App.vue';
import router from './router';
import i18n from './plugins/i18n'
import store from './store'
import linkify from 'vue-linkify'
import '@fortawesome/fontawesome-free/css/all.css'
import '@fortawesome/fontawesome-free/js/all.js'

Vue.config.productionTip = false;
Vue.directive('linkified', linkify)

new Vue({
  router,
  i18n,
  store,
  render: h => h(App)
}).$mount('#app');
