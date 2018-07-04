/**
 * Created by lizhiming on 2017/8/17.
 */
import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/Login.vue'
import Home from '../components/layout/Home.vue'
import AppHome from '../components/layout/AppHome.vue'
import ProjectList from '../components/ProjectList.vue'
import AppSummary from '../components/AppSummary.vue'
import ProtoContent from '../components/ProtoContent.vue'
import Document from '../components/document/Document.vue'
import EditCurrentProtoFile from '../components/EditCurrentProtoContent.vue'
import Content from '../components/Content.vue'
import VersionList from '../components/VersionList.vue'
import HomePage from '../components/Home.vue'


Vue.use(Router);

export default new Router({
    mode: "hash",
    routes: [
        {
            path: '/',
            alias: '/home',
            component: Home,
            redirect: {name: 'HomePage'},
            children: [
                {
                    path: '/projects',
                    name: 'HomePage',
                    component: HomePage
                },
                {
                    path: "/app",
                    name: 'AppHome',
                    component: AppHome,
                    redirect: {name: 'ProtoContent'},
                    children: [
                        {
                            path: '/appSummary',
                            name: 'AppSummary',
                            component: AppSummary
                        },
                        {
                            path: '/protofile',
                            name: 'ProtoContent',
                            component: ProtoContent
                        },
                        {
                            path: '/document',
                            name: 'Document',
                            component: Document
                        },
                        {
                            path: '/editCurrentProtoFile',
                            name: 'EditCurrentProtoFile',
                            component: EditCurrentProtoFile
                        },
                        {
                            path: '/content',
                            name: 'Content',
                            component: Content
                        },
                        {
                            path: '/versionList',
                            name: 'VersionList',
                            component: VersionList
                        }
                    ]
                },
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
        }
    ],
    linkActiveClass: 'active'
});
