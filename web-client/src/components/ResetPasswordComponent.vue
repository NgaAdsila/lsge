<template>
  <div class="reset-password-form">
    <div v-if="isValidLink">
      <b-form @submit.stop.prevent="resetPassword">
        <p class="h4 text-center mb-4 reset-password-label">{{ $t('reset_password.label.title') }}</p>
        <b-form-group label-size="sm"
                      :label="$t('common.label.password')"
                      label-for="input-password">
          <b-form-input type="password" id="input-password" size="sm"
                        v-model="$v.password.$model"
                        maxlength="100"
                        :state="validateState('password')"
                        aria-describedby="password-live-feedback"
                        ref="password"
                        class="grey-text"></b-form-input>
          <b-form-invalid-feedback id="password-live-feedback">
            {{ !$v.password.required
              ? $t('common.validation.required', { name: $t('common.label.password') })
              : (!$v.password.minLength || !$v.password.maxLength
                  ? $t('common.validation.min_max', { name: $t('common.label.password'), min: 8, max: 100 })
                  : $t('common.validation.strict_password')) }}
          </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('register.label.re_password')"
                      label-for="input-re_password">
          <b-form-input type="password" id="input-re_password" size="sm"
                        v-model="$v.rePassword.$model"
                        maxlength="100"
                        :state="validateState('rePassword')"
                        aria-describedby="re_password-live-feedback"
                        ref="rePassword"
                        class="grey-text"></b-form-input>
          <b-form-invalid-feedback id="re_password-live-feedback">
            {{ !$v.rePassword.required
              ? $t('common.validation.required', { name: $t('register.label.re_password') })
              : $t('register.validation.re_password.not_match') }}
          </b-form-invalid-feedback>
        </b-form-group>
        <div class="text-center mt-4">
          <b-button type="submit"
                    :disabled="!!isSubmit"
                    :readonly="!!isSubmit"
                    variant="outline-primary">{{ $t('reset_password.label.submit') }}</b-button>
        </div>
        <div class="text-center mt-4">
          {{ $t('register.label.login_link') }}
          <router-link to="/login">{{ $t('common.label.login') }}</router-link>!
        </div>
      </b-form>
    </div>
    <div v-else class="reset-password-invalid">
      {{ invalidMessage }}
      <div class="text-center mt-4">
        {{ $t('register.label.login_link') }}
        <router-link to="/login">{{ $t('common.label.login') }}</router-link>!
      </div>
    </div>
  </div>
</template>

<script>
import { validationMixin } from 'vuelidate';
import { required, minLength, maxLength } from 'vuelidate/lib/validators';
import { strictPassword, matchPassword } from '@/plugins/vuevalidate';

export default {
  name: "ResetPasswordComponent",
  mixins: [validationMixin],
  props: [
    'isValidLink',
    'invalidMessage',
    'isSubmit'
  ],
  data() {
    return {
      password: '',
      rePassword: ''
    }
  },
  validations: {
    password: {
      required,
      minLength: minLength(8),
      maxLength: maxLength(100),
      strictPassword
    },
    rePassword: {
      required,
      matchPassword
    }
  },
  methods: {
    validateState(name) {
      const { $dirty, $error } = this.$v[name];
      return $dirty ? !$error : null;
    },
    resetPassword() {
      this.$v.$touch();
      if (this.$v.$anyError) {
        this.focusFirstError();
        return;
      }
      this.$emit('resetPassword', { password: this.password })
    },
    focusFirstError() {
      const invalidFields = Object.keys(this.$v.$params)
          .filter(fn => this.$v[fn].$invalid);
      if (invalidFields) {
        this.$refs[invalidFields[0]].$el.focus();
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.reset-password-form {
  margin: 20vh auto;
  max-width: 700px;
  padding: 0.5rem 1rem;
  border: 1px solid lightgray;
  border-radius: 0.5rem;
  box-shadow: 5px 5px lightgray;

  .reset-password-label {
    font-weight: bold;
  }
  .reset-password-invalid {
    text-align: center;
    min-height: 15vh;
    margin-top: 5vh;
  }
}
</style>