import React, { useEffect, useState } from 'react';
import { $generateHtmlFromNodes } from '@lexical/html';
import { AutoFocusPlugin } from '@lexical/react/LexicalAutoFocusPlugin';
import { LexicalComposer } from '@lexical/react/LexicalComposer';
import { ContentEditable } from '@lexical/react/LexicalContentEditable';
import { LexicalErrorBoundary } from '@lexical/react/LexicalErrorBoundary';
import { HistoryPlugin } from '@lexical/react/LexicalHistoryPlugin';
import { RichTextPlugin } from '@lexical/react/LexicalRichTextPlugin';
import { $getRoot, $getSelection } from 'lexical';

import ExampleTheme from './ExampleTheme';
import MyOnChangePlugin from './MyOnChangePlugin';
import ToolbarPlugin from './ToolbarPlugin';
import TreeViewPlugin from './TreeViewPlugin';

import './index.css';

export default function App() {
    const [editorState, setEditorState] = useState();

    function onError(error) {
        console.error(error);
    }

    function onChange(editorState) {
        // Call toJSON on the EditorState object, which produces a serialization safe string
        const editorStateJSON = editorState.toJSON();
        // However, we still have a JavaScript object, so we need to convert it to an actual string with JSON.stringify
        setEditorState(JSON.stringify(editorStateJSON));
    }

    const initialConfig = {
        namespace: 'MyEditor',
        theme: ExampleTheme,
        onError,
    };
    const placeholder = 'Enter some rich text...';

    return (
        <LexicalComposer initialConfig={initialConfig}>
            <div className='editor-container'>
                <ToolbarPlugin />
                <div className='editor-inner'>
                    <RichTextPlugin
                        contentEditable={
                            <ContentEditable
                                className='editor-input'
                                aria-placeholder={placeholder}
                                placeholder={<div className='editor-placeholder'>{placeholder}</div>}
                            />
                        }
                        ErrorBoundary={LexicalErrorBoundary}
                    />
                    <HistoryPlugin />
                    <AutoFocusPlugin />
                    <TreeViewPlugin />
                </div>
            </div>
            <MyOnChangePlugin onChange={onChange} />
        </LexicalComposer>
    );
}
