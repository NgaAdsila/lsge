<template>
    <div class="home-page">
        <section id="most-module">
            <MostModule
                    :modules="mostModules" />
        </section>
    </div>
</template>

<script>
    import MostModule from "../components/home/MostModule";
    export default {
        name: "Home",
        components: {MostModule},
        data() {
            return {
                mostModules: [
                    [{
                        title: 'Module1',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module2',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module3',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                      title: 'Module3-5',
                      icon: 'puzzle',
                      link: '#'
                    }], [{
                        title: 'Module4',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module5',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module6',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                    title: 'Module6-5',
                    icon: 'puzzle',
                    link: '#'
                  }]
                ]
            }
        }
    }
</script>

<style lang="scss" scoped>
    #most-module {
        padding: 5rem 0.5rem 2rem;
        text-align: center;
    }
</style>