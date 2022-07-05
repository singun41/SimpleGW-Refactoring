window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnTemp').setAttribute('onclick', 'updateTempFreeboard()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveFreeboard()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveFreeboard() {
    let docsId = await saveBoard('freeboard');
    if(docsId) {
        saveComplete = true;
        deleteTempFreeboard();   // 정상 등록하면 임시저장 문서를 삭제할 지 묻는다.
        location.href = '/page/freeboard/' + docsId;
    }
}

async function updateTempFreeboard() {
    let docsId = await updateTempBoard('freeboard');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/freeboard/temp/' + docsId;
    }
}

async function deleteTempFreeboard() {
    if(!confirm('현재 임시저장 문서를 삭제하시겠습니까?'))
        return;
    let response = await fetchDelete('freeboard/temp/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
}
