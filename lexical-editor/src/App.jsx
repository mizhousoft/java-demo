import React, { useState } from 'react';
import { $generateNodesFromDOM } from '@lexical/html';
import { AutoFocusPlugin } from '@lexical/react/LexicalAutoFocusPlugin';
import { LexicalComposer } from '@lexical/react/LexicalComposer';
import { ContentEditable } from '@lexical/react/LexicalContentEditable';
import { LexicalErrorBoundary } from '@lexical/react/LexicalErrorBoundary';
import { HistoryPlugin } from '@lexical/react/LexicalHistoryPlugin';
import { RichTextPlugin } from '@lexical/react/LexicalRichTextPlugin';
import { $getRoot } from 'lexical';

import ExampleTheme from './ExampleTheme';
import MyOnChangePlugin from './MyOnChangePlugin';
import ToolbarPlugin from './ToolbarPlugin';
import TreeViewPlugin from './TreeViewPlugin';
import HtmlPlugin from './HtmlPlugin';

import './index.css';

export default function App() {
    const htmlPluginRef = React.createRef();
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

    function initText(editor) {
        const parser = new DOMParser();
        const htmlString = `<p class="editor-paragraph" dir="ltr">
  <span style="white-space: pre-wrap;">paragraph</span>
</p>
<p class="editor-paragraph" dir="ltr">
  <b>
    <strong class="editor-text-bold" style="white-space: pre-wrap;">heading</strong>
  </b>
</p>
<p class="editor-paragraph" dir="ltr">
  <span style="white-space: pre-wrap;">quote</span>
</p>
<p class="editor-paragraph" dir="ltr" style="padding-inline-start: 80px;">
  <span style="white-space: pre-wrap;">paragraph</span>
</p>
<p class="editor-paragraph" dir="ltr">
  <span style="white-space: pre-wrap;">heading</span>
</p>
<p class="editor-paragraph" dir="ltr">
  <span style="white-space: pre-wrap;">quote</span>
</p>`;
        const dom = parser.parseFromString(htmlString, 'text/html');

        const nodes = $generateNodesFromDOM(editor, dom);
        const rootNode = $getRoot();
        for (const node of nodes) {
            try {
                rootNode.append(node);
            } catch (err) {
                continue;
            }
        }

        return rootNode;
    }

    const exportHtml = () => {
        const htmlString = htmlPluginRef.current.exportHtml();
        console.log(htmlString);
    };

    const initialConfig = {
        editorState: initText,
        namespace: 'MyEditor',
        theme: ExampleTheme,
        onError,
    };
    const placeholder = 'Enter some rich text...';

    return (
        <LexicalComposer initialConfig={initialConfig}>
            <button onClick={exportHtml}>Export</button>
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
                    <HtmlPlugin ref={htmlPluginRef} />
                </div>
            </div>
            <MyOnChangePlugin onChange={onChange} />
        </LexicalComposer>
    );
}
