import restApi from '../restApi'

export default {

    //用于界面上传契约文件
    uploadNewProtoFile(request) {
        let url = '/web/contracts/new/';
        return restApi.doUploadPostRequest(url,request);
    },

    //用于界面新建契约文件
    addNewContract(request) {
        let url = '/web/contracts/add?appId=' + request.appId;
        //debugger
        return restApi.doPostRequest(url,request.newContract);
    },

    readFileContentByFileId(request) {
        let url = '/web/contracts?id=' + request.fileId+"&revision="+request.revision;
        return restApi.doGetRequest(url);
    },

    updateFileContentById(request) {
        let url = '/web/contracts/';
        return restApi.doPutRequest(url, request);
    },

    queryProtoFileListByAppId(request){
        let url = '/web/contracts/appid/'+request.appId+"?revision="+request.revisionNumber;
        return  restApi.doGetRequest(url);
    },

    checkProtoContent(request) {
        let url = '/web/contracts/check?appId=' + request.appId;
        return restApi.doPostRequest(url, request.newContract);
    },

    //删除一个 fileId
    deleteProtoFileById(fileId,appId) {
        let url  = '/web/contracts/'+fileId+"?appId="+appId;
        return restApi.doDeleteRequest(url);
    },

    //删除多个 fileIds
    deleteProtoFileByMultiIds(request) {
        let url  = '/web/contracts?appId=' + request.appId;
        return restApi.doPostRequest(url, request.ids);
    },

    deployProtoFileByIds(fileId, appId) {
        let url  = '/web/contracts/deploy?fileId='+fileId+"&appId="+appId;
        return restApi.doDeployRequest(url);
    }

}
