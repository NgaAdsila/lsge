<template>
    <div class="profile-component">
        <div class="page-header header-filter" data-parallax="true">
            <MeteorShower />
            <b-button class="cover-update-btn"
                      size="sm" variant="primary">
                <b-icon icon="camera"></b-icon>
            </b-button>
        </div>
        <div class="main main-raised">
            <div class="profile-content">
                <div class="container">
                    <div class="row">
                        <div class="col-md-6 ml-auto mr-auto">
                            <div class="profile">
                                <div class="avatar">
                                    <b-img v-if="profile && profile.avatar"
                                           :src="profile.avatar"
                                           class="img-raised rounded-circle img-fluid"></b-img>
                                    <div v-else class="profile-avatar-default">
                                        <Avatar :color="profile ? profile.color : ''"
                                                :name="profile ? profile.name : ''"
                                                default-color="#28a745"
                                                size="160"
                                        />
                                    </div>
                                    <b-button class="avatar-update-btn"
                                              size="sm" variant="primary">
                                        <b-icon icon="camera"></b-icon>
                                    </b-button>
                                </div>
                                <div class="name">
                                    <h3 class="title">{{ profile ? profile.name : '' }}</h3>
                                    <h6>{{ profile ? profile.username : '' }}</h6>
                                    <div class="social-link">
                                        <a href="https://facebook.com" target="_blank"><i class="fab fa-facebook"></i></a>
                                        <a href="https://twitter.com" target="_blank"><i class="fab fa-twitter"></i></a>
                                        <a href="https://youtube.com" target="_blank"><i class="fab fa-youtube"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="text-center mt-4">
                        <b-button size="md"
                                  v-b-modal="'edit-profile-modal'"
                                  variant="success">Edit Profile</b-button>
                    </div>
                    <div class="description text-center">
                        <p>An artist of considerable range, Chet Faker — the name taken by Melbourne-raised, Brooklyn-based Nick Murphy — writes, performs and records all of his own music, giving it a warm, intimate feel with a solid groove structure. </p>
                    </div>
                </div>
            </div>
        </div>
        <EditProfileModal :profile="profile" :isLoading="isLoading" @save="save" />
    </div>
</template>

<script>
    import EditProfileModal from "../modal/profile/EditProfile";
    import Avatar from "../common/Avatar";
    import MeteorShower from "../common/MeteorShower";
    export default {
        name: "UserProfile",
        components: {MeteorShower, Avatar, EditProfileModal},
        props: [
            'profile',
            'isLoading'
        ],
        methods: {
            save(profileModel) {
                this.$emit('save', profileModel)
            }
        }
    }
</script>

<style lang="scss" scoped>
    html *{
        -webkit-font-smoothing: antialiased;
    }
    .profile-component {
        .h6, h6 {
            font-size: .75rem !important;
            font-weight: 500;
            font-family: Roboto,Helvetica,Arial,sans-serif;
            line-height: 1.5em;
            text-transform: uppercase;
        }

        .name h6 {
            margin-top: 10px;
            margin-bottom: 10px;
        }

        .navbar {
            border: 0;
            border-radius: 3px;
            padding: .625rem 0;
            margin-bottom: 20px;
            color: #555;
            background-color: #fff!important;
            box-shadow: 0 4px 18px 0 rgba(0,0,0,.12), 0 7px 10px -5px rgba(0,0,0,.15) !important;
            z-index: 1000 !important;
            transition: all 150ms ease 0s;

        }

        a .material-icons {
            vertical-align: middle;
        }

        .page-header {
            height: 300px;
            background-position:center;
            background-size: cover;
            margin: 0;
            padding: 0;
            border: 0;
            display: flex;
            align-items: center;

            .cover-update-btn {
                position: absolute;
                top: 0.5rem;
                right: 0.5rem;
                z-index: 2;
                width: 2.5rem;
                height: 2.5rem;
                border-radius: 50%;
            }
        }

        .header-filter {
            &:after, &:before {
                position: absolute;
                z-index: 1;
                width: 100%;
                height: 300px;
                display: block;
                left: 0;
                top: 0;
                content: "";
            }
            &:before {
                /*background: linear-gradient(rgba(255,255,255,.3), rgba(255,255,255,.3)), url(/card/profile-cover.png) no-repeat center center;*/
                /*background-size: cover;*/
                /*background-blend-mode: darken;*/
                /*-ms-background-size: cover;*/
                /*-o-background-size: cover;*/
                /*-moz-background-size: cover;*/
                /*-webkit-background-size: cover;*/
            }
        }

        .main-raised {
            margin: -60px 30px 0;
            border-radius: 6px;
            box-shadow: 0 16px 24px 2px rgba(0,0,0,.14), 0 6px 30px 5px rgba(0,0,0,.12), 0 8px 10px -5px rgba(0,0,0,.2);
        }

        .main {
            background: #FFF;
            position: relative;
            padding-bottom: 2rem;
            min-height: calc(100vh - 300px);
            z-index: 3;
        }

        .profile {
            text-align: center;

            img, .profile-avatar-default {
                width: 160px;
                height: 160px;
                margin: 0 auto;
                -webkit-transform: translate3d(0,-50%,0);
                -moz-transform: translate3d(0,-50%,0);
                -o-transform: translate3d(0,-50%,0);
                -ms-transform: translate3d(0,-50%,0);
                transform: translate3d(0,-50%,0);
            }
            .name {
                margin-top: -80px;

                .social-link {
                    svg {
                        margin: 0 1rem;
                        color: #999;

                        &:hover {
                            color: rgba(0,0,0,.87);
                        }
                    }
                }
            }
            .avatar {
                position: relative;

                .avatar-update-btn {
                    position: absolute;
                    bottom: calc(50% + 1rem);
                    left: calc(50% + 3rem);
                    width: 2.5rem;
                    height: 2.5rem;
                    border-radius: 50%;
                }
            }
        }

        .img-raised {
            box-shadow: 0 5px 15px -8px rgba(0,0,0,.24), 0 8px 10px -5px rgba(0,0,0,.2);
        }

        .rounded-circle {
            border-radius: 50%!important;
            object-fit: cover;
        }

        .img-fluid, .img-thumbnail {
            max-width: 100%;
            height: auto;
        }

        .title {
            margin-top: 30px;
            margin-bottom: 25px;
            min-height: 32px;
            color: #3C4858;
            font-weight: 700;
            font-family: "Roboto Slab","Times New Roman",serif;
        }

        .description {
            margin: 1.071rem auto 0;
            color: #999;
            font-weight: 300;
        }

        p {
            font-size: 14px;
            margin: 0 0 10px;
        }
    }

</style>