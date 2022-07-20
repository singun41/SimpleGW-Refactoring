window.addEventListener('DOMContentLoaded', () => {
    getCount();
});

async function getCount() {
    let response = await fetchGet('user/my-dayoff-count');
    let result = await response.json();
    if(response.ok) {
        let cnt = result.obj;
        document.getElementById('sum').innerText = cnt.usable;
        document.getElementById('use').innerText = cnt.use;
        document.getElementById('cnt').innerText = cnt.usable - cnt.use;
    }
}
