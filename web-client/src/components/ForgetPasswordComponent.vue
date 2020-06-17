<template>
    <div class="forget-password-form">
        <b-form @submit.stop.prevent="onSubmit">
            <p class="h4 text-center mb-4 login-label">{{ $t('forget_password.label.title') }}</p>
            <b-form-group label-size="sm"
                          :label="$t('forget_password.label.email')"
                          label-for="input-email">
                <b-form-input id="input-email" size="sm"
                              v-model="$v.email.$model"
                              maxlength="40"
                              :state="validateState('email')"
                              aria-describedby="email-live-feedback"
                              ref="email"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="email-live-feedback">
                    {{ !$v.email.required
                    ? $t('common.validation.required', { name: $t('forget_password.label.email') })
                    : $t('common.validation.invalid_email') }}
                </b-form-invalid-feedback>
            </b-form-group>
            <div class="text-center mt-4">
                <b-button type="submit"
                          :disabled="isSubmit"
                          :readonly="isSubmit"
                          variant="outline-primary">{{ $t('forget_password.label.submit') }}</b-button>
            </div>
            <div class="text-center mt-4">
                {{ $t('register.label.login_link') }}
                <router-link to="/login">{{ $t('common.label.login') }}</router-link>!
            </div>
        </b-form>
    </div>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, email } from 'vuelidate/lib/validators';

    export default {
        name: "ForgetPasswordComponent",
        mixins: [validationMixin],
        props: [
            'isSubmit'
        ],
        data() {
            return {
                email: ''
            }
        },
        validations: {
            email: {
                required,
                email
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v[name];
                return $dirty ? !$error : null;
            },
            onSubmit() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }
                this.$emit('forgetPassword', {
                    email: this.email
                })
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
    .forget-password-form {
        margin: 20vh auto;
        max-width: 700px;
        padding: 0.5rem 1rem;
        border: 1px solid lightgray;
        border-radius: 0.5rem;
        box-shadow: 5px 5px lightgray;

        .register-label {
            font-weight: bold;
        }
    }
</style>