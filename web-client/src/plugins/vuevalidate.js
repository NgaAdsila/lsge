import Vue from 'vue';
import Vuelidate from 'vuelidate';
import { helpers } from 'vuelidate/lib/validators';

Vue.use(Vuelidate);

export const strictPassword = helpers.regex('strictPassword', /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!-/:-@[-`{-~])[!-~]{8,40}$/);