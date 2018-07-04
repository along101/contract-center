<template>
    <div class="wrapper">
        <v-head></v-head>
        <!--<v-sidebar></v-sidebar>-->
        <div class="content">
            <transition name="move" mode="out-in">
                <router-view></router-view>
            </transition>
            <!--<v-footer></v-footer>-->
        </div>
    </div>
</template>

<script>

    import vHead from './Header.vue';
    import vFooter from './Footer.vue'
    import {mapGetters} from 'vuex'

    export default {
        components: {
            vHead, vFooter
            //vHead
        },

        computed: {
            ...mapGetters({
                promptMessage: 'getPromptMessage'
            })
        },

        watch: {
            promptMessage: function (newMessage) {
                //console.log(newMessage);
                if (newMessage.code != null) {
                    if (newMessage.code >= 0) {
                        this.$message.success(newMessage.details);
                    } else {
                        this.$message.error(newMessage.details);
                    }
                }
            }
        },
        created() {
            console.log('home created');
            this.$store.dispatch('getCommonConfig');
        },
    }
</script>
