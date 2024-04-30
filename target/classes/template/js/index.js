function getAllRecords() {
    axios.get('/csv/all')
    .then(response => {
        const allRecordsContainer = document.getElementById('allRecordsContainer');
        allRecordsContainer.innerHTML = '<h2>Все записи из CSV файла:</h2>';
        response.data.forEach(record => {
            allRecordsContainer.innerHTML += `<p>${JSON.stringify(record)}</p>`;
        });
    })
    .catch(error => {
        const allRecordsContainer = document.getElementById('allRecordsContainer');
        allRecordsContainer.innerHTML = `<p>Ошибка при получении записей: ${error.response.status} ${error.response.statusText}</p>`;
    });
    }

    function getRecordById() {
        const recordId = document.getElementById('recordId').value;
        axios.get(`/csv/${recordId}`)
        .then(response => {
            const singleRecordContainer = document.getElementById('singleRecordContainer');
            singleRecordContainer.innerHTML = `<h2>Запись с идентификатором ${recordId}:</h2><p>${JSON.stringify(response.data)}</p>`;
        })
        .catch(error => {
            const singleRecordContainer = document.getElementById('singleRecordContainer');
            singleRecordContainer.innerHTML = `<p>Ошибка при получении записи: ${error.response.status} ${error.response.statusText}</p>`;
        });
    }

    function addNewRecord() {
        const newRecordData = document.getElementById('newRecordData').value;
        axios.post('/csv', { data: newRecordData })
        .then(response => {
            console.log('New record added successfully');
            const successContainer = document.getElementById('addRecordSuccess');
            successContainer.innerHTML = '<p>Новая запись успешно добавлена!</p>';
            // Очистка полей ввода
            document.getElementById('newRecordData').value = '';
            document.getElementById('errorContainer').innerHTML = '';
        })
        .catch(error => {
            console.error('Error:', error);
            const errorContainer = document.getElementById('errorContainer');
            errorContainer.innerHTML = `<p>Ошибка при добавлении новой записи: ${error.response.status} ${error.response.statusText}</p>`;
        });
    }

    function updateRecord() {
        const updateRecordId = document.getElementById('updateRecordId').value;
        const updatedRecordData = document.getElementById('updatedRecordData').value;
        axios.put(`/csv/${updateRecordId}`, { data: updatedRecordData })
        .then(response => {
            console.log('Record updated successfully');
            const successContainer = document.getElementById('updateRecordSuccess');
            successContainer.innerHTML = `<p>Запись с идентификатором ${updateRecordId} успешно обновлена!</p>`;
            // Очистка полей ввода
            document.getElementById('updateRecordId').value = '';
            document.getElementById('updatedRecordData').value = '';
            document.getElementById('errorContainer').innerHTML = '';
        })
        .catch(error => {
            console.error('Error:', error);
            const errorContainer = document.getElementById('errorContainer');
            errorContainer.innerHTML = `<p>Ошибка при обновлении записи: ${error.response.status} ${error.response.statusText}</p>`;
        });
    }

    function deleteRecord() {
        const deleteRecordId = document.getElementById('deleteRecordId').value;
        axios.delete(`/csv/${deleteRecordId}`)
        .then(response => {

            console.log('Record deleted successfully');
            const successContainer = document.getElementById('deleteRecordSuccess');
            successContainer.innerHTML = `<p>Запись с идентификатором ${deleteRecordId} успешно удалена!</p>`;
            // Очистка полей ввода
            document.getElementById('deleteRecordId').value = '';
            document.getElementById('errorContainer').innerHTML = '';
        })
        .catch(error => {
            console.error('Error:', error);
            const errorContainer = document.getElementById('errorContainer');
            errorContainer.innerHTML = `<p>Ошибка при удалении записи: ${error.response.status} ${error.response.statusText}</p>`;
        });
    }