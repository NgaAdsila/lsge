<template>
    <b-modal id="manager-user-list-update-role"
             centered
             scrollable
             body-class="manager-user-model-body"
             :title="$t('manager.action.update_role')"
             :ok-title="$t('common.label.save')"
             :ok-disabled="$v.$error"
             @ok="onOk"
             @cancel="cancel">
        <div v-if="userList && userList.length">
            <b-form-group v-for="(user, index) in userList"
                          label-size="sm"
                          :label="user.name"
                          :key="index"
                          :label-for="'input-roles-' + index">
                <b-form-select :id="'input-roles-' + index" size="sm"
                               v-model="$v.userList.$each[index].roles.$model"
                               :options="roleOptions"
                               :select-size="2"
                               :state="validateState('roles', index)"
                               :aria-describedby="'roles-live-feedback' + index"
                               :ref="'roles' + index"
                               multiple></b-form-select>
                <b-form-invalid-feedback :id="'roles-live-feedback' + index">
                    {{ $t('common.validation.required', { name: $t('manager.label.roles') }) }}
                </b-form-invalid-feedback>
            </b-form-group>
        </div>
    </b-modal>
</template>

<script>
import {validationMixin} from 'vuelidate';
import {required} from "vuelidate/lib/validators";

export default {
    name: "ManagerUserListUpdateRole",
    mixins: [validationMixin],
    props: [
        'users',
        'roleOptions'
    ],
    data() {
        return {
            userList: []
        }
    },
    watch: {
        users: function(val) {
            this.userList = val.map(u => {
                return {
                    id: u.id,
                    name: u.fullName,
                    roles: u.roles ? u.roles.map(r => r.value) : []
                }
            })
        }
    },
    validations: {
        userList: {
            $each: {
                roles: {
                    required
                }
            }
        }
    },
    methods: {
        validateState(name, index) {
            const { $dirty, $error } = this.$v.userList.$each[index][name]
            return $dirty ? !$error : null
        },
        onOk() {
            this.$v.userList.$touch()
            if (this.$v.userList.$anyError) {
              return false
            }
            this.$emit('onOk', this.userList)
        },
        cancel() {
            this.$emit('cancel')
        }
    }
}
</script>

<style lang="scss">
#manager-user-list-update-role {
    .manager-user-model-body {
        max-height: 700px;
        overscroll-behavior: contain;
        &::-webkit-scrollbar {
            width: 0.5rem;
        }
        &::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 0.3rem transparent;
        }
        &::-webkit-scrollbar-thumb {
            background-color: transparent;
            border-radius: 0.5rem;
            outline: none;
        }
        &:hover::-webkit-scrollbar-thumb {
            background-color: darkgrey;
        }
    }
}
</style>