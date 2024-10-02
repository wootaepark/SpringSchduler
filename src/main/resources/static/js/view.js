let selectedScheduleId = null; // 수정할 일정 ID

// 일정 데이터를 불러오는 함수
async function fetchSchedules() {
    try {
        const response = await fetch('/api/schedules'); // 백엔드에서 일정 데이터를 가져오는 API 엔드포인트
        const schedules = await response.json();

        // 일정 데이터를 화면에 표시
        displaySchedules(schedules);
    } catch (error) {
        console.error('Error fetching schedules:', error);
        document.getElementById('loadingMessage').innerText = '일정을 불러오지 못했습니다.';
    }
}

// 일정 데이터를 화면에 표시하는 함수
function displaySchedules(schedules) {
    const container = document.getElementById('scheduleContainer');
    container.innerHTML = ''; // 기존 내용을 지움
    document.getElementById('loadingMessage').style.display = 'none'; // 로딩 메시지 숨기기

    // 일정 데이터를 카드 형태로 삽입
    schedules.forEach(schedule => {
        const card = document.createElement('div');
        card.classList.add('schedule-card');

        // 이스케이프 함수 (textarea 를 출력하기 위한)
        function escapeHtml(str) {
            return str
                .replace(/&/g, '&amp;')   // &를 &amp;로 변환
                .replace(/</g, '&lt;')    // <를 &lt;로 변환
                .replace(/>/g, '&gt;')    // >를 &gt;로 변환
                .replace(/"/g, '&quot;')  // "를 &quot;로 변환
                .replace(/'/g, '&#39;')   // '를 &#39;로 변환
                .replace(/\n/g, '\\n');    // 줄 바꿈을 이스케이프
        }

        card.innerHTML = `
                <div class="schedule-header" onclick="openInfoModal('${schedule.title}', '${escapeHtml(schedule.content)}', '${schedule.username}', '${schedule.scheduledDate}', '${schedule.updatedAt}')">
                    ${schedule.title}
                </div>
                <div class="schedule-footer">
                    <div><b>작성자: </b>${schedule.username}</div>
                    <div style="color: red;"><b>예정 날짜: </b>${new Date(schedule.scheduledDate).toLocaleDateString()}</div>
                    <div><b>최종 수정일: </b>${new Date(schedule.updatedAt).toLocaleDateString()}</div>
                </div>
                <button onclick="openEditModal(${schedule.id}, '${schedule.username}', '${schedule.title}', '${schedule.content}', '${schedule.scheduledDate}')">수정</button>
                <button onclick="deleteSchedule(${schedule.id})">삭제</button>
            `;

        container.appendChild(card);
    });
}

function openInfoModal(title, content, username, scheduledDate, updatedAt) {
    document.getElementById('infoModalTitle').innerText = title;

    // 줄 바꿈(\n)을 <br>로 변환해서 HTML 형식으로 출력
    document.getElementById('infoModalContent').innerHTML = content.replace(/\n/g, '<br>');

    document.getElementById('infoModalUsername').innerText = `작성자: ${username}`;
    document.getElementById('infoModalScheduledDate').innerText = `일정 날짜: ${new Date(scheduledDate).toLocaleDateString()}`;
    document.getElementById('infoModalUpdatedAt').innerText = `최종 수정일: ${new Date(updatedAt).toLocaleDateString()}`;

    document.getElementById('infoModal').style.display = 'flex'; // 모달 표시
}





// 정보 모달 닫기
document.getElementById('infoModalClose').onclick = function () {
    document.getElementById('infoModal').style.display = 'none';
};

// 페이지가 로드될 때 일정을 불러옴
window.onload = function () {
    fetchSchedules();
};

// 모달창 닫기 버튼(X) 처리
document.querySelector('.close').onclick = function () {
    document.getElementById('modal').style.display = 'none';
};


// 수정 모달 열기
function openEditModal(id, username, title, content, date) {
    selectedScheduleId = id;
    document.getElementById('editUsername').value = username;
    document.getElementById('editTitle').value = title;
    document.getElementById('editContent').value = content;
    document.getElementById('editDate').value = new Date(date).toLocaleDateString();
    document.getElementById('editPassword').value = ''; // 비밀번호 필드 초기화
    document.getElementById('modal').style.display = 'flex';
}

// 수정 모달 닫기
document.getElementById('cancel').onclick = function () {
    document.getElementById('modal').style.display = 'none';
};

// 수정 폼 제출
document.getElementById('editForm').onsubmit = async function (e) {
    e.preventDefault();

    const password = document.getElementById('editPassword').value;
    const updatedData = {
        username: document.getElementById('editUsername').value,
        title: document.getElementById('editTitle').value,
        content: document.getElementById('editContent').value,
        scheduledDate: document.getElementById('editDate').value, // 날짜 정보 추가
        password: password
    };

    try {
        const response = await fetch(`/api/schedules/${selectedScheduleId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData) // 비밀번호 포함하여 body로 전송
        });

        const result = await response.json(); // JSON 형태로 응답 받기

        if (result === -1) {
            alert('비밀번호가 일치하지 않습니다. 다시 시도해 주세요.');
        } else {
            alert(`일정이 성공적으로 수정되었습니다. 수정된 ID: ${result}`);
            document.getElementById('modal').style.display = 'none';
            fetchSchedules(); // 수정 후 다시 일정 불러오기
        }
    } catch (error) {
        console.error('Error updating schedule:', error);
    }
};


let selectedScheduleIdForDelete = null; // 삭제할 일정 ID

// 삭제 모달 열기
function openDeleteModal(id) {
    selectedScheduleIdForDelete = id;
    document.getElementById('deletePassword').value = ''; // 비밀번호 필드 초기화
    document.getElementById('deleteModal').style.display = 'flex';
}

// 삭제 모달 닫기
function closeDeleteModal() {
    document.getElementById('deleteModal').style.display = 'none';
}

// 삭제 폼 제출
document.getElementById('deleteForm').onsubmit = async function (e) {
    e.preventDefault();

    const password = document.getElementById('deletePassword').value;

    if (confirm('정말로 이 일정을 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`/api/schedules/${selectedScheduleIdForDelete}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ password }) // 비밀번호를 본문에 포함하여 전송
            });

            const result = await response.json();
            if (result !== -1) {
                alert('일정이 삭제되었습니다.');
                fetchSchedules(); // 삭제 후 다시 일정 불러오기
            } else {
                alert('잘못된 비밀번호를 입력했습니다.');
            }
        } catch (error) {
            console.error('Error deleting schedule:', error);
        }
    }

    closeDeleteModal(); // 모달 닫기
};

// 일정 삭제 버튼 클릭 시 모달 열기
async function deleteSchedule(id) {
    openDeleteModal(id);
}


// 페이지가 로드될 때 일정을 불러옴
window.onload = function () {
    fetchSchedules();
};

// 모달창 닫기 버튼(X) 처리
document.querySelector('.close').onclick = function () {
    document.getElementById('modal').style.display = 'none';
};