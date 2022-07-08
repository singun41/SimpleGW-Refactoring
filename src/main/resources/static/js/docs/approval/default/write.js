window.addEventListener('DOMContentLoaded', event => {
    copyDefault();
});

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

async function saveTemp() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };
    let docsId = await saveTempApprovalDocs(params);
    
    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/' + docsType.toLowerCase() + '/' + docsId;
    }
}

function copyDefault() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
