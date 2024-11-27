const savePostForm = document.querySelector('#save-post')
const savePostBtn = document.querySelector('#save-post-btn')

export default async function readFileAsBase64(postImage) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = () => resolve(reader.result.split(',')[1]);
        reader.onerror = error => reject(error);

        reader.readAsDataURL(postImage);
    });
}

savePostBtn.onclick = async event => {
    event.preventDefault();

    const formData = new FormData(savePostForm);
    const formObject = Object.fromEntries(formData.entries());

    const errorMsg = document.getElementById('file-error');

    const postImages = formData.getAll('base64Image');
    if (postImages.length > 0) {
        if (postImages.length > 3) {
            errorMsg.style.display = 'block';
            return;
        }

        formObject.base64Image = [];
        for (let img of postImages) {
            const base64 = await readFileAsBase64(img);
            formObject.base64Image.push(base64);
        }
    } else formObject.base64Image = null;

    await fetchToCreatePost(formObject);
};

async function fetchToCreatePost(post) {
    const response = await fetch('http://localhost:8080/posts/', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(post)
    })

    const data = await response.json();

    console.log("Результат фетча: ", data)
}



