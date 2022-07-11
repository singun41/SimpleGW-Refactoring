async function save() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };
    let docsId = await saveApprovalDocs(params);

    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/' + docsType.toLowerCase() + '/' + docsId;
    }
}

async function updateTemp() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };

    let docsId = await updateTempApprovalDocs(params);
    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/' + docsType.toLowerCase() + '/temp/' + docsId;
    }
}
