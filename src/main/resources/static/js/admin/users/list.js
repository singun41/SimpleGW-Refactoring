document.addEventListener('DOMContentLoaded', () => {
    getUsers();
});

function buildDatatable() {
    $('#datatables').DataTable({
        language: {
            paginate: {
                previous: '<i class="fa-solid fa-chevron-left"></i>',
                next: '<i class="fa-solid fa-chevron-right"></i>'
            }
        },
        order: [4, 'asc'],   // order by names.
        ordering: true,
        columnDefs: [
            // 0 = id hidden field.
            { targets: 0, width: '0%' },
            { targets: 1, width: '14%' },
            { targets: 2, width: '20%' },
            { targets: 3, width: '14%' },
            { targets: 4, width: '14%' },
            { targets: 5, width: '14%' },
            { targets: 6, width: '8%' },
            { targets: 7, width: '8%' },
            { targets: 8, width: '8%' }
        ],
        scrollX: true,
        scrollXInner: '100%',
        fixedHeader: true,
        scrollY: 650,
        pageLength: 100
    });
}

async function getUsers() {
    let params = {
        isRetired: document.getElementById('chkIsRetired').checked
    };
    let response = await fetchGetParams('user/all', params);
    let result = await response.json();

    destroyDataTable();
    removeDatalist();

    if(response.ok) {
        Array.from(result.obj).forEach(e => {
            let tr = document.createElement('tr');

            let id = document.createElement('td');
            let userId = document.createElement('td');
            let team = document.createElement('td');
            let jobTitle = document.createElement('td');
            let name = document.createElement('td');
            let role = document.createElement('td');
            let enabled = document.createElement('td');
            let edit = document.createElement('td');
            let btnEdit = document.createElement('button');
            let pw = document.createElement('td');
            let btnPw = document.createElement('button');

            btnEdit.setAttribute('onclick', 'openProfile("' + e.id + '")');
            btnEdit.classList.add('btn', 'btn-outline-secondary', 'btn-sm');
            btnEdit.innerHTML = '<i class="fa-solid fa-up-right-from-square"></i>';
            edit.append(btnEdit);

            btnPw.setAttribute('onclick', 'openPw("' + e.id + '")');
            btnPw.classList.add('btn', 'btn-outline-secondary', 'btn-sm');
            btnPw.innerHTML = '<i class="fa-solid fa-up-right-from-square"></i>';
            pw.append(btnPw);

            id.innerText = e.id;
            id.classList.add('d-none');

            userId.innerText = e.userId;
            team.innerText = e.team;
            jobTitle.innerText = e.jobTitle;
            name.innerText = e.name;
            
            role.innerText = e.role;
            if(e.role.toLowerCase() !== 'user') {
                role.classList.add('class', 'text-primary');
            }
            if(e.role.toLowerCase() === 'admin') {
                role.classList.remove('text-primary');
                role.classList.add('text-danger');
            }

            if(e.enabled) {
                enabled.classList.add('text-success');
                enabled.innerHTML = '<i class="fa-solid fa-check"></i>';
            } else {
                enabled.classList.add('text-danger');
                enabled.innerHTML = '<i class="fa-solid fa-user-lock"></i>';
            }

            tr.append(id, userId, team, jobTitle, name, role, enabled, edit, pw);
            datalist.append(tr);
        });
    }
    buildDatatable();
}

function openProfile(id) {
    let option = "width=1330, height=550";
    window.open('user/profiles/' + id, '', option);
}

function openNew() {
    let option = "width=1330, height=550";
    window.open('user/new', '', option);
}

function openPw(id) {
    let option = "width=500, height=250";
    window.open('user/pw/' + id, '', option);
}
