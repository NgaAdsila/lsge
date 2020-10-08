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
                      label-for="input-avatarFile">
            <div v-show="profile && profile.avatar" id="avatar-preview">
                <b-img v-if="profile && profile.avatar" :src="profile.avatar" />
            </div>
            <b-form-file id="input-avatarFile"
                         v-model="$v.profile.avatarFile.$model"
                         @change="onAvatarFileChange"
                         :state="validateState('avatarFile')"
                         aria-describedby="avatarFile-live-feedback"
                         ref="avatarFile"></b-form-file>
            <b-form-invalid-feedback id="avatarFile-live-feedback">
              {{ !$v.profile.avatarFile.imageType
                    ? $t('common.validation.invalid_image_type', {types: allowedExtensionTypes})
                    : $t('common.validation.invalid_image_size', {name: $t('common.label.avatar'), max: maxFileSize}) }}
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
    import { strictUserName, imageType, imageSize } from '@/plugins/vuevalidate';
    import {FILE_UPLOAD} from "@/services/constants";

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
                    imageType,
                    imageSize
                }
            }
        },
        computed: {
            allowedExtensionTypes: function () {
                return FILE_UPLOAD.ALLOWED_EXTENSION_TITLE
            },
            maxFileSize: function () {
                return FILE_UPLOAD.MAX_SIZE_TITLE
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
                    return;
                }
                this.$emit('save');
            },
            onAvatarFileChange(e) {
                this.$v.$touch();
                if (this.$v.profile.avatarFile.$anyError) {
                  return
                }
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