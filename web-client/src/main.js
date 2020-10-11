import '@babel/polyfill';
import 'mutationobserver-shim';
import Vue from 'vue';
import './plugins/bootstrap-vue';
import App from './App.vue';
import router from './router';
import i18n from './plugins/i18n'
import store from './store'
import linkify from 'vue-linkify'

Vue.config.productionTip = false;
Vue.directive('linkified', linkify)

Vue.filter('reverse', function(value) {
  // slice to make a copy of array, then reverse the copy
  return value.slice().reverse();
});

new Vue({
  router,
  i18n,
  store,
  render: h => h(App)
}).$mount('#app');
