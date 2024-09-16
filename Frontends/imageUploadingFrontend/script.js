const uploadButton = document.getElementById('uploadButton');
const imageInput = document.getElementById('imageInput');
const uploadedImages = document.getElementById('uploadedImages');

// Function to fetch and display uploaded images
const getUploadedImages = () => {
    fetch('http://localhost:8080/api/v1/s3', {
        method: 'GET',
    })
        .then(response => response.json())
        .then(urls => {
            // Clear previous images
            uploadedImages.innerHTML = '';
            // Display each image
            urls.forEach(url => {
                const img = document.createElement('img');
                img.src = url;
                img.classList.add('uploaded-image'); // Add CSS class for styling
                uploadedImages.appendChild(img);
            });
        })
        .catch(error => {
            console.error('Error fetching images:', error);
        });
};

// Event listener for the upload button
uploadButton.addEventListener('click', () => {
    const file = imageInput.files[0]; // Get selected file
    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        fetch('http://localhost:8080/api/v1/s3', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                // Immediately fetch and display updated images after upload
                getUploadedImages();
                // Clear file input for new uploads
                imageInput.value = '';
            })
            .catch(error => {
                console.error('Error uploading image:', error);
            });
    }
});

// Initial fetch of uploaded images on page load
getUploadedImages();
