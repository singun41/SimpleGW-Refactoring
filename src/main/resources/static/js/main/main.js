document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    connectSse();

    notificationModal = new bootstrap.Modal(document.getElementById('notification'), { keyboard: false });

    alarmMessageModal = new bootstrap.Modal(document.getElementById('alarmMessage'), { keyboard: false });
    getTodayAlarms();
    setInterval(() => { showAlarm(); }, 1000 * 60);
    setInterval(() => { document.getElementById('datetimeText').innerHTML = dayjs().format('YY. MM. DD. (ddd) a hh:mm:ss'); }, 1000);
});
let alarmMessageModal;

window.addEventListener('beforeunload', event => {   // document가 아니라 window에서 처리해야 함.
    // 페이지를 나갈 때 sse 연결을 끊어서 서버에서 불필요한 데이터 전송을 막는다.
    event.preventDefault();
    disconnectSse();
});

window.addEventListener('message', receiveMsgFromChild);
function receiveMsgFromChild(e) {   // content.js로부터 메시지 수신
    if(e.data === 'alarm') {
        getTodayAlarms();
    }
}

const iframePage = document.getElementById('page');
function page(url) {
    iframePage.setAttribute('src', '/page/' + url);
}

function sendMsgToChild(msg) {   // iFrame으로 띄워져 있는 페이지의 js 함수 호출. 여기서는 content.js
    iframePage.contentWindow.postMessage(msg, '*');
}


function connectSse() {
    let sse = new EventSource(location.protocol + '//' + location.host + '/sse/connect');
    sse.onmessage = (event) => {
        let result = JSON.parse(event.data);
        
        if(result.NOTICE) {
            sendMsgToChild('notice');

        } else if(result.FREEBOARD) {
            sendMsgToChild('freeboard');

        } else if(result.APPROVER) {
            document.getElementById('notiContent').innerHTML = '새로운 결재 요청문서가 도착했습니다.';
            sendMsgToChild('approver');
            notificationModal.show();

        } else if(result.REFERRER) {
            document.getElementById('notiContent').innerHTML = '새로운 결재 참조문서가 도착했습니다.';
            sendMsgToChild('referrer');
            notificationModal.show();

        } else if(result.CONFIRMED) {
            document.getElementById('notiContent').innerHTML = '결재문서 ' + result.title + '(' + result.type + ', No. ' + result.docsId + ') 의 결재가 <strong class="text-success">승인</strong>되었습니다.';
            sendMsgToChild('confirmed');
            notificationModal.show();

        } else if(result.REJECTED) {
            document.getElementById('notiContent').innerHTML = '결재문서 ' + result.title + '(' + result.type + ', No. ' + result.docsId + ') 의 결재가 <strong class="text-danger">반려</strong>되었습니다.';
            sendMsgToChild('rejected');
            notificationModal.show();

        }
    };
}

async function disconnectSse() {
    await fetchGet('sse/disconnect');
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
