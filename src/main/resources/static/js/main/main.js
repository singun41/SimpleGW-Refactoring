document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    connectSse();

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
