import Vue from 'vue';
import Vuelidate from 'vuelidate';
import { helpers } from 'vuelidate/lib/validators';

Vue.use(Vuelidate);

export const strictPassword = helpers.regex('strictPassword', /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!-/:-@[-`{-~])[!-~]{8,100}$/);

export const strictUserName = helpers.regex('strictUserName', /^(?=.{4,40}$)[a-zA-Z]+[a-zA-Z0-9._+@]+$/);

export const matchPassword = (value, vm) => (value === vm.password);

export const notMatchOldPassword = (value, vm) => (value !== vm.oldPassword);