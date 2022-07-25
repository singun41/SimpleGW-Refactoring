document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    connectSse();

    alarmApprovalModal = new bootstrap.Modal(document.getElementById('alarmApproval'), { keyboard: true });
    alarmMessageModal = new bootstrap.Modal(document.getElementById('alarmMessage'), { keyboard: true });
    notificationModal = new bootstrap.Modal(document.getElementById('notification'), { keyboard: true });
    profileModal = new bootstrap.Modal(document.getElementById('profile'), { keyboard: true });

    getTodayAlarms();
    setInterval(() => { showAlarm(); }, 1000 * 60);
    setInterval(() => { document.getElementById('datetimeText').innerHTML = dayjs().format('YY. MM. DD. (ddd) a hh:mm:ss'); }, 1000);
});
let alarmApprovalModal;
let alarmMessageModal;
let notificationModal;

window.addEventListener('beforeunload', event => {   // document가 아니라 window에서 처리해야 함.
    // 페이지를 나갈 때 sse 연결을 끊어서 서버에서 불필요한 데이터 전송을 막는다.
    event.preventDefault();
    disconnectSse();
});

window.addEventListener('message', receiveMsgFromChild);
function receiveMsgFromChild(e) {   // content.js로부터 메시지 수신
    if(e.data === 'alarm') {
        openAlarmPage()
    } else if(e.data === 'notification') {
        showNotifications();
    } else if(e.data === 'profile') {
        showProfile();
    }
}

const iframePage = document.getElementById('page');
function page(url) {
    iframePage.setAttribute('src', `/page/${url}`);
}

function sendMsgToChild(msg) {   // iFrame으로 띄워져 있는 페이지의 js 함수 호출. 여기서는 content.js
    iframePage.contentWindow.postMessage(msg, '*');
}


function connectSse() {
    let sse = new EventSource(`${location.protocol}//${location.host}/sse/connect`);
    sse.onmessage = (event) => {
        let result = JSON.parse(event.data);
        
        if(result.NOTICE) {
            sendMsgToChild('notice');

        } else if(result.FREEBOARD) {
            sendMsgToChild('freeboard');

        } else if(result.APPROVER) {
            document.getElementById('approvalContent').innerHTML = '새 결재 요청문서가 도착했습니다.';
            sendMsgToChild('approver');
            alarmApprovalModal.show();

        } else if(result.REFERRER) {
            document.getElementById('approvalContent').innerHTML = '새 결재 참조문서가 도착했습니다.';
            sendMsgToChild('referrer');
            alarmApprovalModal.show();

        } else if(result.CONFIRMED) {
            document.getElementById('approvalContent').innerHTML = result.content;
            sendMsgToChild('confirmed');
            alarmApprovalModal.show();

        } else if(result.REJECTED) {
            document.getElementById('approvalContent').innerHTML = result.content;
            sendMsgToChild('rejected');
            alarmApprovalModal.show();

        } else if(result.NOTIFICATION) {
            sendMsgToChild('notification');
        }
    };
}

async function disconnectSse() {
    await fetchGet('sse/disconnect');
}

function openAlarmPage() {
    openPopup('/page/alarm');
}

let todayAlarms = [];
async function getTodayAlarms() {
    let response = await fetchGet('alarm/today');
    let result = await response.json();

    todayAlarms = [];   // alarm 신규 생성시 호출하므로 기존 데이터를 초기화한다.
    if(response.ok) {
        Array.from(result.obj).forEach(e => { todayAlarms.push(e) });
        showAlarm();
    }
}

function showAlarm() {
    todayAlarms.forEach(e => {
        if(dayjs().format('HH:mm') === e.alarmTime.substr(0, 5)) {
            document.getElementById('alarmTitle').innerText = e.title;
            document.getElementById('alarmRemarks').value = e.remarks;
            alarmMessageModal.show();
        }
    });
}

async function showNotifications() {
    let response = await fetchGet('notification/list');
    let result = await response.json();

    if(response.ok) {
        let notiContent = document.getElementById('notiContent');
        notiContent.innerHTML = '';

        Array.from(result.obj).forEach(e => {
            notiContent.innerHTML += `${e}<br><br>`;
        });
    }
    notificationModal.show();
    setTimeout(() => { sendMsgToChild('notification'); }, 1000 * 2);
}

async function showProfile() {
    let response = await fetchGet('profile');
    let result = await response.json();

    if(response.ok) {
        let profile = result.obj;
        document.getElementById('profileName').value = profile.name;
        document.getElementById('profileNameEng').value = profile.nameEng;
        document.getElementById('profileJobTitle').value = profile.jobTitle;
        document.getElementById('profileTeam').value = profile.team;
        document.getElementById('profileTel').value = profile.tel;
        document.getElementById('profileEmail').value = profile.email;
        document.getElementById('profileMobile').value = profile.mobile;
        document.getElementById('profileDuration').value = profile.duration;
        document.getElementById('profileHireDate').value = dayjs(profile.dateHire).format('YY. MM. DD.');
    }
    profileModal.show();
}

async function updateProfile() {
    let portraitInput = document.getElementById('portraitInput');
    if(portraitInput.files[0] !== undefined && portraitInput.files[0] !== null) {
        let formData = new FormData;
        formData.append('img', portraitInput.files[0]);

        let response = await fetchFormData('portrait', formData);
        if(response.ok)
            document.getElementById('userPortrait').setAttribute('src', '/portrait');
    }

    let params = {
        nameEng: document.getElementById('profileNameEng').value,
        mobile: document.getElementById('profileMobile').value
    };
    let response = await fetchPatchParams('profile', params);
    let result = await response.json();
    alert(result.msg);
}

const collapsedMenus = Array.from(document.getElementsByClassName('collapse'));
let isCollapsed = true;
function toggleCollapse() {
    if(isCollapsed) {
        document.getElementById('toggleCollapse').innerHTML = '<i class="fa-solid fa-toggle-on"></i>';
        collapsedMenus.forEach(e => {
            let bsCollapse = new bootstrap.Collapse(e, { toggle: false });
            bsCollapse.show();
        });
    } else {
        document.getElementById('toggleCollapse').innerHTML = '<i class="fa-solid fa-toggle-off"></i>';
        collapsedMenus.forEach(e => {
            let bsCollapse = new bootstrap.Collapse(e, { toggle: false });
            bsCollapse.hide();
        });
    }
    isCollapsed = !isCollapsed;
}
