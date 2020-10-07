<template>
    <b-form @submit.stop.prevent="save" class="profile-form">
        <b-form-group label-size="sm"
                      :label="$t('common.label.username')"
                      label-for="input-username">
            <b-form-input id="input-username" size="sm"
                          v-model="$v.profile.username.$model"
                          maxlength="40"
                          readonly
                          disabled></b-form-input>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.name')"
                      label-for="input-name">
            <b-form-input id="input-name" size="sm"
                          v-model="$v.profile.name.$model"
                          maxlength="40"
                          :state="validateState('name')"
                          aria-describedby="name-live-feedback"
                          ref="name"></b-form-input>
            <b-form-invalid-feedback id="name-live-feedback">
                {{ $t('common.validation.required', { name: $t('common.label.name') }) }}
            </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.email')"
                      label-for="input-email">
            <b-form-input type="email" id="input-email" size="sm"
                          v-model="$v.profile.email.$model"
                          :state="validateState('email')"
                          aria-describedby="email-live-feedback"
                          ref="email"></b-form-input>
            <b-form-invalid-feedback id="email-live-feedback">
                {{ !$v.profile.email.required
                ? $t('common.validation.required', { name: $t('common.label.email') })
                : $t('common.validation.invalid_email') }}
            </b-form-invalid-feedback>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.color')"
                      label-for="input-color">
            <b-form-input type="color" id="input-color" size="sm"
                          v-model="$v.profile.color.$model"
                          ref="color"></b-form-input>
        </b-form-group>
        <b-form-group label-size="sm"
                      :label="$t('common.label.avatar')"
                      label-for="input-avatar">
            <div v-show="profile && profile.avatar" id="avatar-preview">
                <b-img v-if="profile && profile.avatar" :src="profile.avatar" />
            </div>
            <b-form-file id="input-avatar"
                         v-model="$v.profile.avatarFile.$model"
                         plain
                         @change="onAvatarFileChange"
                         :state="validateState('avatarFile')"
                         aria-describedby="avatar-live-feedback"
                         ref="avatar"></b-form-file>
            <b-form-invalid-feedback id="avatar-live-feedback">
                {{ $t('common.validation.invalid_image') }}
            </b-form-invalid-feedback>
        </b-form-group>
        <div class="text-center mt-4">
            <b-button type="submit"
                      :disabled="!!isLoading"
                      :readonly="!!isLoading"
                      variant="outline-primary">{{ $t('common.label.save') }}</b-button>
        </div>
    </b-form>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, minLength, maxLength, email } from 'vuelidate/lib/validators';
    import { strictUserName, imageType } from '@/plugins/vuevalidate';

    export default {
        name: "ProfileGeneralInformation",
        mixins: [validationMixin],
        props: [
            'profile',
            'isLoading'
        ],
        validations: {
            profile: {
                name: {
                    required
                },
                username: {
                    required,
                    minLength: minLength(4),
                    maxLength: maxLength(40),
                    strictUserName
                },
                email: {
                    required,
                    email
                },
                color: {},
                avatarFile: {
                    imageType
                }
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v.profile[name];
                return $dirty ? !$error : null;
            },
            save() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }
                this.$emit('save');
            },
            focusFirstError() {
                const invalidFields = Object.keys(this.$v.$params)
                    .filter(fn => this.$v[fn].$invalid);
                if (invalidFields) {
                    this.$refs[invalidFields[0]].$el.focus();
                }
            },
            onAvatarFileChange(e) {
                const file = e.target.files[0]
                this.profile.avatar = URL.createObjectURL(file)
            }
        }
    }
</script>

<style lang="scss" scoped>
.profile-form {
    #avatar-preview {
        margin-bottom: 0.5rem;
        img {
            width: 6rem;
            height: 6rem;
            border-radius: 100%;
            object-fit: cover;
        }
    }
}
</style>