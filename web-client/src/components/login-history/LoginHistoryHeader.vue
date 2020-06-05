<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="login-history-header">
        <h4 class="text-center text-uppercase font-weight-bold">{{ ($t('common.label.login_history')) }}</h4>
        <b-row>
            <b-col class="col-auto col-sm-6 font-weight-bold login-history-total-record">
                {{ $t('common.message.total_record', { number: paging.total }) }}
            </b-col>
            <b-col class="col-auto col-sm-6">
                <b-input-group class="search-key-word">
                    <b-form-input v-model="req.keyword"
                                  maxlength="100"
                                  :placeholder="$t('common.placeholder.keyword')"
                                  @keydown.enter.native="search"></b-form-input>
                    <template v-slot:append>
                        <b-button v-b-toggle.sidebar-search><b-icon icon="list"></b-icon></b-button>
                        <b-sidebar id="sidebar-search" :title="$t('login_history.label.search_form')" right shadow>
                            <b-form @submit.stop.prevent="search" class="text-justify search-form">
                                <hr/>
                                <b-form-group :label="$t('common.label.keyword')"
                                              label-for="input-keyword">
                                    <b-form-input id="input-keyword" size="sm"
                                                  v-model="req.keyword"
                                                  :placeholder="$t('common.placeholder.keyword')"
                                                  maxlength="100"
                                                  class="grey-text"></b-form-input>
                                </b-form-group>
                                <b-form-group :label="$t('login_history.label.login_time')"
                                              label-for="input-timeFrom">
                                    <b-form-datepicker id="input-timeFrom"
                                                       class="datepicker-form"
                                                       v-model="req.timeFrom"
                                                       size="sm"
                                                       today-button
                                                       reset-button
                                                       close-button
                                                       locale="en"
                                                       :date-format-options="{ day: '2-digit', month: '2-digit', year: 'numeric' }"
                                                       placeholder="Choose time from"></b-form-datepicker>
                                    <span class="font-weight-bold search-range-symbol">~</span>
                                    <b-form-datepicker v-model="req.timeTo"
                                                       class="datepicker-form"
                                                       size="sm"
                                                       today-button
                                                       reset-button
                                                       close-button
                                                       locale="en"
                                                       :date-format-options="{ day: '2-digit', month: '2-digit', year: 'numeric' }"
                                                       placeholder="Choose time to"></b-form-datepicker>
                                </b-form-group>
                                <hr/>
                                <div class="text-center mt-4">
                                    <b-button type="submit"
                                              v-b-toggle.sidebar-search
                                              variant="outline-primary">{{ $t('common.label.search') }}</b-button>
                                </div>
                            </b-form>
                        </b-sidebar>
                    </template>
                </b-input-group>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    export default {
        name: "LoginHistoryHeader",
        props: [
            'req',
            'paging'
        ],
        methods: {
            search() {
                this.$emit('search', this.req);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .login-history-header {
        margin: 1rem;
        .login-history-total-record {
            line-height: 2.375rem;
        }
        .search-key-word {
            max-width: 22rem;

            @media (min-width: 574px) {
                float: right!important;
            }

            input {
                @media (max-width: 486px) {
                    display: none;
                }
            }
        }
    }
    #sidebar-search {
        .search-form {
            padding: 0 1rem;
        }
        .search-range-symbol {
            margin-left: 49%;
        }
    }
</style>