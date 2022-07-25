document.addEventListener('DOMContentLoaded', () => {
    initializePostIt();

    getNotices();
    getFreeBoardList();
    getTempDocsCount();
    getProceedDocsCnt();
    getApproverDocsCnt();
    getReferrerDocsCnt();
    getNotiCount();
});

window.addEventListener('message', receiveMsgFromParent);   // 'document'가 아니라 'window'다.
function receiveMsgFromParent(e) {   // main.js로부터 메시지 수신: server sent event 알림 용도
    // 전달받은 메시지 = e.data
    if(e.data === 'notice') {
        getNotices();

    } else if(e.data === 'freeboard') {
        getFreeBoardList();

    } else if(e.data === 'approver') {
        getApproverDocsCnt();

    } else if(e.data === 'referrer') {
        getReferrerDocsCnt();

    } else if(e.data ==='confirmed' || e.data === 'rejected') {
        getProceedDocsCnt();
        getNotiCount();
    
    } else if(e.data === 'notification') {
        getNotiCount();
    }
}

function sendMsgToParent(msg) {   // main.js에게 메시지 전달
    window.parent.postMessage(msg, '*');
}

const innerCalendar = document.getElementById('innerCalendar');
function sendMsgToChild(msg) {   // calendar.js에게 메시지 전달
    innerCalendar.contentWindow.postMessage(msg, '*');
}

function updateCalendar() {
    if(confirm('캘린더 api를 다시 호출하여 업데이트 하시겠습니까?'))
        sendMsgToChild('updateCalendar');
}

document.getElementById('btnPostitSave').addEventListener('click', () => {
    let e = document.getElementById('btnPostitSave');
    let tooltip = bootstrap.Tooltip.getInstance(e);
    
    e.setAttribute('data-bs-original-title', 'Saved !');
    tooltip.show();

    setTimeout(() => {
        tooltip.hide();
        e.setAttribute('data-bs-original-title', 'Save');
    }, 1500);
});

async function getNotices() {
    let response = await fetchGet('notice/main-list');
    let result = await response.json();
    
    let list = document.getElementById('notice');
    if(!list)
        return;
    
    while(list.hasChildNodes())
        list.removeChild(list.firstChild);

    Array.from(result.obj).forEach(e => {
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        let a = document.createElement('a');

        a.setAttribute('role', 'button');
        a.setAttribute('onclick', `openPopup("/page/notice/${e.id}")`);
        a.classList.add('text-decoration-none', 'text-dark');
        a.innerText = e.title;
        
        if(e.new) {
            let span = document.createElement('span');
            span.classList.add('badge', 'rounded-pill', 'bg-danger', 'float-end');
            span.innerText = 'New';
            a.append(span);
        }

        td.append(a);
        tr.append(td);
        list.append(tr);
    });
}

async function getFreeBoardList() {
    let response = await fetchGet('freeboard/main-list');
    let result = await response.json();
    
    let list = document.getElementById('freeboard');
    if(!list)
        return;
    
    while(list.hasChildNodes())
        list.removeChild(list.firstChild);

    Array.from(result.obj).forEach(e => {
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        let a = document.createElement('a');
        
        a.setAttribute('role', 'button');
        a.setAttribute('onclick', `openPopup("/page/freeboard/${e.id}")`);
        a.classList.add('text-decoration-none', 'text-dark');
        a.innerText = e.title;
        
        if(e.new) {
            let span = document.createElement('span');
            span.classList.add('badge', 'rounded-pill', 'bg-danger', 'float-end');
            span.innerText = 'New';
            a.append(span);
        }

        td.append(a);
        tr.append(td);
        list.append(tr);
    });
}

async function getTempDocsCount() {
    let response = await fetchGet('docs/temp/count');
    let result = await response.json();
    if(response.ok)
        document.getElementById('cntTempDocs').innerText = result.obj === 0 ? '' : result.obj;
}

function initializePostIt() {
    Array.from(document.getElementsByTagName('textarea')).forEach(e => {
        e.setAttribute('placeholder', '간단히 작성하고 활용하세요.');
    });
}

function popupAlarmPage() {
    sendMsgToParent('alarm');
}

async function getProceedDocsCnt() {
    let response = await fetchGet('approval/proceed-cnt');
    let result = await response.json();
    if(response.ok)
        document.getElementById('cntProceed').innerText = result.obj === 0 ? '' : result.obj;
}

async function getApproverDocsCnt() {
    let response = await fetchGet('approval/approver-cnt');
    let result = await response.json();
    if(response.ok)
        document.getElementById('cntApprover').innerText = result.obj === 0 ? '' : result.obj;
}

async function getReferrerDocsCnt() {
    let response = await fetchGet('approval/referrer-cnt');
    let result = await response.json();
    if(response.ok)
        document.getElementById('cntReferrer').innerText = result.obj === 0 ? '' : result.obj;
}

async function getNotiCount() {
    let response = await fetchGet('notification/count');
    let result = await response.json();
    if(response.ok)
        document.getElementById('cntNotification').innerText = result.obj === 0 ? '' : result.obj;
}

function showNotification() {
    sendMsgToParent('notification');
}

function showProfile() {
    sendMsgToParent('profile');
}
