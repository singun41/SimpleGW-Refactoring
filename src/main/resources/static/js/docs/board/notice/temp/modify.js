window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnTemp').setAttribute('onclick', 'updateTempNotice()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveNotice()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveNotice() {
    let docsId = await saveBoard('notice');
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        deleteTempNotice();   // 정상 등록하면 임시저장 문서를 삭제할 지 묻는다.
        location.href = '/page/notice/' + docsId;
    }
}

async function updateTempNotice() {
    let docsId = await updateTempBoard('notice');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/notice/temp/' + docsId;
    }
}

async function deleteTempNotice() {
    if(!confirm('현재 임시저장 문서를 삭제하시겠습니까?'))
        return;
    let response = await fetchDelete('notice/temp/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
}
