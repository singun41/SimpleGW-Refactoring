let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

// 여기서 docsId는 const 로 선언하면 안 됨.
// 임시저장이 아닌 등록 후에 받는 docsId는 다른 값이기 때문.
const docsType = document.getElementById('docsType').innerText;

async function save() {
    let docsId = await saveBoard();

    if(docsId) {
        saveComplete = true;
        deleteTempFreeboard();   // 정상 등록하면 임시저장 문서를 삭제할 지 묻는다.
        location.href = '/page/' + docsType.toLowerCase() + '/' + docsId;
    }
}

async function saveBoard() {
    if(!confirm('등록하시겠습니까?'))
        return 0;

    let params = {
        title: document.getElementById('title').value,
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };

    let response = await fetchPostParams(docsType.toLowerCase(), params);
    let result = await response.json();

    if(response.ok) {
        // 문서저장 후 첨부파일 저장 진행.
        if(arrFile.length === 0) {
            alert(result.msg);
            return result.obj;
        }

        let uploadResponse = await uploadFiles(result.obj);   // 리턴받은 obj 속성에 문서번호가 담겨있음.
        let uploadResult = await uploadResponse.json();
        alert(uploadResult.msg);   // 파일 첨부 결과 메시지 띄우기.

        if(uploadResponse.ok) {   // 첨부파일 완료 후 문서번호 리턴.
            return result.obj;
        } else {
            return 0;
        }

    } else {   // 문서 저장 실패시 메시지 띄우기.
        alert(result.msg);
        return 0;
    }
}


async function deleteTemp() {
    if(!confirm('현재 임시저장 문서를 삭제하시겠습니까?'))
        return;

    let response = await fetchDelete(docsType.toLowerCase() + '/temp/' + document.getElementById('docsId').innerText);
    let result = await response.json();
    alert(result.msg);
}

async function updateTemp() {
    if(!confirm('임시저장 하시겠습니까?' + '\n' + '첨부파일은 저장되지 않습니다.'))
        return;
    
    let docsId = document.getElementById('docsId').innerText;
    let params = {
        title: document.getElementById('title').value,
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };

    let response = await fetchPatchParams(docsType.toLowerCase() + '/temp/' + docsId, params);
    let result = await response.json();
    alert(result.msg);

    if(response.ok) {
        saveComplete = true;        
        location.href = '/page/' + docsType.toLowerCase() + '/temp/' + docsId;
    }
}
