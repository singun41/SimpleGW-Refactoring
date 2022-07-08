window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'saveNotice()');
});

async function saveNotice() {
    let docsId = await saveBoard();
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        deleteTemp();   // 정상 등록하면 임시저장 문서를 삭제할 지 묻는다.
        location.href = '/page/' + docsType.toLowerCase() + '/' + docsId;
    }
}
