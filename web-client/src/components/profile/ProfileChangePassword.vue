<template>
    <b-form @submit.stop.prevent="changePassword" class="profile-form">
        <b-form-group label-size="sm"
                      :label="$t('profile.label.old_password')"
                      label-for="input-password">
            <b-form-input type="password" id="input-old-password" size="sm"
                          v-model="$v.oldPassword.$model"
                          maxlength="100"
                          :state="validateState('oldPassword')"
                          aria-describedby="old-password-live-feedback"
                          ref="oldPassword"
                          class="grey-text"></b-form-input>
            <b-form-invalid-feedback id="old-password-live-feedback">
                {{ !$v.oldPassword.required
                ? $t('common.validation.required', { name: $t('profile.label.old_password') })
                : $t('common.validation.min_max', { name: $t('profile.label.old_password'), min: 8, max: 100 }) }}
            </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('profile.label.new_password')"
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
                ? $t('common.validation.required', { name: $t('profile.label.new_password') })
                : (!$v.password.minLength || !$v.password.maxLength
                ? $t('common.validation.min_max', { name: $t('profile.label.new_password'), min: 8, max: 100 })
                : (!$v.password.strictPassword ? $t('common.validation.strict_password') : $t('profile.validation.match_old_password'))) }}
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
                      :disabled="!!isLoading"
                      :readonly="!!isLoading"
                      variant="outline-primary">{{ $t('profile.label.change_password') }}</b-button>
        </div>
    </b-form>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, minLength, maxLength } from 'vuelidate/lib/validators';
    import { strictPassword, matchPassword, notMatchOldPassword } from '../../plugins/vuevalidate';

    export default {
        name: "ProfileChangePassword",
        mixins: [validationMixin],
        data() {
            return {
                oldPassword: null,
                password: null,
                rePassword: null
            }
        },
        props: [
            'isLoading'
        ],
        validations: {
            oldPassword: {
                required,
                minLength: minLength(8),
                maxLength: maxLength(100),
                strictPassword
            },
            password: {
                required,
                minLength: minLength(8),
                maxLength: maxLength(100),
                strictPassword,
                notMatchOldPassword
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
            changePassword() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }
                this.$emit('changePassword', {
                    oldPassword: this.oldPassword,
                    password: this.password
                });
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

<style scoped>

</style>