import React from 'react';
import RichEditor from './RichEditor';

export default function App() {
    const editorRef = React.createRef(undefined);

    const exportHtml = () => {
        const htmlString = editorRef.current.exportHtml();
        console.log(htmlString);
    };

    return (
        <>
            <button onClick={exportHtml}>Export</button>

            <RichEditor ref={editorRef} />
        </>
    );
}
