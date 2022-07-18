document.addEventListener('DOMContentLoaded', () => {
    initialization();
});

window.addEventListener('message', receiveMsgFromParent);   // 'document'가 아니라 'window'다.
function receiveMsgFromParent(e) {   // calendar.js로부터 메시지 수신: calendar manual update 용도.
    // 전달받은 메시지 = e.data
    if(e.data === 'updateCalendar')
        updateCalendar();
}

let calendar;
function initialization() {
    let e = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(e, {
        // height: 700,
        aspectRatio: 1.2,
        initialView: 'dayGridMonth',
        expandRows: true,
        headerToolbar: {
            start: 'title',
            center: '',
            end: 'prev,next today'
        },
        slotMinTime: '00:00',
        slotMaxTime: '24:00',

        navLinks: false, // can click day/week names to navigate views
        editable: false,
        selectable: false,
        nowIndicator: true,
        dayMaxEvents: true, // allow "more" link when too many events

        locale: 'ko',
        weekNumbers: true,

        // event에 mouse over시 description을 tooltip으로 보여준다.
        // tippy.js 의 cdn을 추가해야 함.
        eventDidMount: info => {
            let div = document.createElement('div');
            div.innerText = info.event.title;
            tippy(info.el, { content: div });
        }
    });
    calendar.render();
    getHolidays();

    document.getElementsByClassName('fc-prev-button')[0].addEventListener('click', () => {
        getHolidays();
    });
    document.getElementsByClassName('fc-next-button')[0].addEventListener('click', () => {
        getHolidays();
    });
    document.getElementsByClassName('fc-today-button')[0].addEventListener('click', () => {
        getHolidays();
    });
}

function addEvent(id, title, start, end, holiday) {
    calendar.addEvent({
        id: id,
        title: title,
        start: start,
        end: end,
        color: (holiday === true ? '#f1a9a0' : '#ffffff'),
        textColor: '#343a40'
    });
}

async function getHolidays() {
    let evtSources = calendar.getEvents();
    for(let i=0; i<evtSources.length; i++) {
        evtSources[i].remove();
    }

    let year = calendar.getDate().getFullYear();
    let month = calendar.getDate().getMonth() + 1;

    let response = await fetchGet(`api/calendar/holidays/${year}/${month}`);
    let result = await response.json();
    if(response.ok)
        Array.from(result).forEach(e => { addEvent(e.index, e.title, e.dateStart, e.dateEnd, e.holiday); });
}

async function updateCalendar() {
    let response = await fetchGet('api/calendar/manual');
    if(response.ok)
        getHolidays();
}
