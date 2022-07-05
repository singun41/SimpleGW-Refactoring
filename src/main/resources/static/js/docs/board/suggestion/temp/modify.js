window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnTemp').setAttribute('onclick', 'updateTempSuggestion()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveSuggestion()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveSuggestion() {
    let docsId = await saveBoard('suggestion');
    if(docsId) {
        saveComplete = true;
        deleteTempSuggestion();   // 정상 등록하면 임시저장 문서를 삭제할 지 묻는다.
        location.href = '/page/suggestion/' + docsId;
    }
}

async function updateTempSuggestion() {
    let docsId = await updateTempBoard('suggestion');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/suggestion/temp/' + docsId;
    }
}

async function deleteTempSuggestion() {
    if(!confirm('현재 임시저장 문서를 삭제하시겠습니까?'))
        return;
    let response = await fetchDelete('suggestion/temp/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
}
