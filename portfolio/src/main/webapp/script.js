function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!', '안녕!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}
/** Fetches tasks from the server and adds them to the DOM. */
function loadTasks() {
    fetch('/tasks', {method: 'GET'}).then(response => response.json()).then((tasks) => {
      const taskListElement = document.getElementById('task-list');
      tasks.forEach((task) => {
        taskListElement.appendChild(createTaskElement(task));
      })
    });
}
function attachElement(){
    console.log(documents.getElementById("title").value); 
    const taskElement = document.getElementById('task-list');
    taskListElement.appendChild(createTaskElement);
}
/** Creates an element that represents a task, including its delete button. */
function createTaskElement(task) {
    const taskElement = document.createElement('li');
    taskElement.className = 'task';
  
    const titleElement = document.createElement('span');
    titleElement.innerText = task.title;
  
    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.innerText = 'Delete';
    deleteButtonElement.addEventListener('click', () => {
      deleteTask(task);
  
      // Remove the task from the DOM.
      taskElement.remove();
    });
  
    taskElement.appendChild(titleElement);
    taskElement.appendChild(deleteButtonElement);
    return taskElement;
  }
  
/** Tells the server to delete the task. */
function deleteTask(task) {
    fetch('/tasks?' +
         new URLSearchParams({task_id: task.id}),
         {method: 'DELETE'}
         );
}

function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map'),
        {center: {lat: 33.97919947216425, lng: -117.32849493862577}, zoom: 16});

    const trexMarker = new google.maps.Marker({
      position: {lat: 33.97919947216425, lng: -117.32849493862577},
      map: map,
      title: 'Cycle Bun'
    });
} 
  