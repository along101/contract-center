import restApi from '../restApi'

export default {

    getProjectById(projectId) {
        let url = '/web/sites/' + projectId;
        return restApi.doGetRequest(url);
    },
    //get all by page
    getAllProjects(request = {}) {
        let url = '/web/projects/page?page=' + (request.page - 1) + '&size=' + request.pageSize;
        return restApi.doGetRequest(url);
    },
    deleteProject(request = {}){
        let url = "/web/projects/" + request.projectId;
        return restApi.doDeleteRequest(url);
    },
    createProject(request = {}) {
        let url = "/web/projects";
        return restApi.doPostRequest(url, request.newProject);
    },

    searchProjectsByAppName(request = {}) {
        let url = '/web/projects/fuzzy/appname?appName=' + request.appName;
        return restApi.doGetRequest(url);
    },

    searchProjects(request = {}) {
        let url = '/web/projects/condition?page=' + (request.page - 1) + '&size=' + request.pageSize;

        //判断查询参数是否为空
        if (request.queryApp.appId != null) {
            url = url + '&appId=' + request.queryApp.appId;
        }

        url = url + '&userWorkNumber=' + request.queryApp.userWorkNumber;
        url = url + '&appName=' + request.queryApp.appName;

        if (request.queryApp.orgId != null) {
            url = url + '&orgId=' + request.queryApp.orgId;
        }

        console.log("api search query url: " + url);
        return restApi.doGetRequest(url);
    },

    updateProject(request = {}) {
        let url = "/web/projects";
        return restApi.doPutRequest(url, request.newProject)
    },

    getAllOrgs(request = {}) {
        let url = '/web/projects/orgs/all';
        return restApi.doGetRequest(url);
    },

    getProjectByAppId(request = {}) {
        let url = '/web/projects/app/' + request.appId;
        return restApi.doGetRequest(url);
    },

}
