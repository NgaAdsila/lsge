import Vue from 'vue';

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

Vue.filter('reverse', function(value) {
    // slice to make a copy of array, then reverse the copy
    return value.slice().reverse();
});

Vue.filter('uppercase', function (value) {
    return value ? value.toUpperCase().trim() : value;
});