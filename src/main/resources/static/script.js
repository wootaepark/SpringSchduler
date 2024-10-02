document.addEventListener("DOMContentLoaded", function() {
    const calendar = document.getElementById('calendar');
    const prevMonthBtn = document.getElementById('prevMonth');
    const nextMonthBtn = document.getElementById('nextMonth');
    const modal = document.getElementById('modal');
    const closeModal = document.querySelector('.close');
    const cancelBtn = document.getElementById('cancel');
    const eventForm = document.getElementById('eventForm');
    const selectedDateDisplay = document.getElementById('selectedDate'); // 선택된 날짜를 표시할 요소

    let currentDate = new Date();
    let selectedDate = null;

    // 달력 그리기 함수
    function renderCalendar(date) {
        calendar.innerHTML = ''; // 달력 초기화

        const year = date.getFullYear();
        const month = date.getMonth();
        const firstDayOfMonth = new Date(year, month, 1).getDay();
        const lastDateOfMonth = new Date(year, month + 1, 0).getDate();

        // 기존에 추가한 달/연도 타이틀 제거 (중복 방지)
        const oldTitle = document.querySelector('.month-title');
        if (oldTitle) {
            oldTitle.remove();
        }

        // 년/월 타이틀을 텍스트로 출력
        const monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
        const title = document.createElement('div');
        title.className = 'month-title';
        title.textContent = `${year}년 ${monthNames[month]}`;
        const container = document.querySelector('.container');
        container.insertBefore(title, calendar); // 달력 위에 년/월을 삽입

        // 요일 헤더 추가
        const weekDays = ['일', '월', '화', '수', '목', '금', '토'];
        weekDays.forEach(day => {
            const dayDiv = document.createElement('div');
            dayDiv.className = 'day-header';
            dayDiv.textContent = day;
            calendar.appendChild(dayDiv);
        });

        // 첫 번째 주의 빈 칸 채우기
        for (let i = 0; i < firstDayOfMonth; i++) {
            const emptyDiv = document.createElement('div');
            emptyDiv.className = 'empty';
            calendar.appendChild(emptyDiv);
        }

        // 날짜 채우기
        for (let day = 1; day <= lastDateOfMonth; day++) {
            const dayDiv = document.createElement('div');
            dayDiv.className = 'day';
            dayDiv.textContent = day;

            // 현재 날짜 표시 (테두리만)
            const today = new Date();
            if (day === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                dayDiv.classList.add('current-day'); // 'current-day' 클래스 추가


            }
            // 날짜 클릭 이벤트
            dayDiv.addEventListener('click', function() {
                selectedDate = new Date(year, month, day);
                openModal();
            });

            calendar.appendChild(dayDiv);
        }
    }

    // 모달 열기 함수
    function openModal() {
        const formattedDate = `${selectedDate.getFullYear()}년 ${selectedDate.getMonth() + 1}월 ${selectedDate.getDate()}일`;
        selectedDateDisplay.textContent = formattedDate; // 선택된 날짜를 표시
        modal.style.display = 'flex';
    }

    // 모달 닫기 함수
    function closeModalFunc() {
        modal.style.display = 'none';
        eventForm.reset(); // 폼 초기화
    }

    // 이전 달로 이동
    prevMonthBtn.addEventListener('click', function() {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar(currentDate);
    });

    // 다음 달로 이동
    nextMonthBtn.addEventListener('click', function() {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar(currentDate);
    });

    // 모달 닫기 이벤트
    closeModal.addEventListener('click', closeModalFunc);
    cancelBtn.addEventListener('click', closeModalFunc);


    // 날짜 형식을 yyyy-MM-dd로 변환하는 함수
    function formatDateToDB(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고 2자리로 맞춤
        const day = String(date.getDate()).padStart(2, '0'); // 일도 2자리로 맞춤
        return `${year}-${month}-${day}`;
    }

    // 폼 제출 이벤트
    eventForm.addEventListener('submit', function(event) {
        event.preventDefault();

        // 입력값 가져오기
        const scheduledDate = formatDateToDB(selectedDate);
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const passwordConfirm = document.getElementById('passwordConfirm').value;
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        // 비밀번호 확인
        if (password !== passwordConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        // 서버로 보낼 데이터 구성
        const formData = {

            username : username,
            email : email,
            password: password,
            title: title,
            content: content,
            scheduledDate: scheduledDate,
        };

        // 간단한 데이터 등록 (콘솔로 출력)
        console.log({
            scheduledDate,
            username,
            password,
            title,
            content
        });

        // POST 요청으로 서버에 데이터 전송
        fetch('/api/schedules', {  // 적절한 백엔드 URL로 수정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        }).then(response => {
            if (response.ok) {  // 상태 코드가 200-299 범위인 경우 성공
                return response.json();
            } else {
                throw new Error('Network response was not ok.');
            }
        })
            .then(data => {
                alert("일정이 성공적으로 등록되었습니다!");
                closeModalFunc(); // 모달 닫기
            })
            .catch(error => {
                console.error('Error:', error);
                alert("서버 오류가 발생했습니다. 나중에 다시 시도해주세요.");
            });


    });

    // 페이지 로드 시 현재 날짜로 달력 표시
    renderCalendar(currentDate);
});

