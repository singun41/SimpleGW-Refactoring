async function update() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };
    let docsId = await updateApprovalDocs(params);
    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/' + docsType.toLowerCase() + '/' + docsId;
    }
}
