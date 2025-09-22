NO DOCKER VÁ EM SETTINGS E DEIXE O MODO 
Expose daemon on tcp://localhost:2375 without TLS
DEIXE HABILITADO




# GitWit Agent
<img src="https://gitwitdev.github.io/gifs/new-repo.gif" align="right" width="500" />
<img src="https://gitwitdev.github.io/images/gitwit-agent-overview.png" align="right" width="500" />

GitWit is a container-based agent specialized in making useful commits to git repositories. Given a description (i.e. "implement dark mode") it either checks out a repository or creates a new one, makes these changes, and then pushes the changes to master. (Skip to [How it works](#how-it-works).)

Given there exist [a few agents](https://github.com/jamesmurdza/awesome-ai-devtools#pr-agents) with a similar purpose—**why is GitWit different?** GitWit interacts with the filesystem in a temporary sandbox and thus can run any shell command. It _writes code that writes code_. This makes it very flexible and repurposable for a number of interesting use cases.

This agent is also live for testing at [app.gitwit.dev](https://app.gitwit.dev) and has generated over 1000 repositories!

### Contents
- [How to run it](#how-to-run-it)
- [Commands](#commands)
- [Examples](#examples)
- [Demos](#demos)
- [Additional configuration](#additional-configuration)
- [LLM configuration](#llm-configuration)
- [How it works](#how-it-works)

## How to run it

Before you start:
1. You need NodeJS (v18).
2. You need Docker.
3. The agent will access to your GitHub account via [personal access token](https://github.com/settings/tokens).
4. You need an [OpenAI API key](https://platform.openai.com/account/api-keys).

Setup:
1. `git clone https://github.com/jamesmurdza/gitwit && cd gitwit` to clone this repository.
2. `cp .env.example .env` to create a .env file. Update **GITHUB_USERNAME**, **GITHUB_TOKEN** and **OPENAI_API_KEY** with your values.
3. Start Docker! (GitWit creates a temporary Docker container for each run.) The easiest way to do this locally is with Docker Desktop. See here to connect to a remote docker server.
4. `docker pull node:latest` to download the base Docker image.
5. `run npm install` to install dependencies.

You are ready to go!

## Commands

Generate a new GitHub repository:

`npm run start`

Generate a repository with the same name and description as the last run:

`npm run start -- --again`

Generate a repository with the same name, description, and build script as the last run:

`npm run start -- --offline`

Debug the build script from the last run:

`npm run start -- --offline --debug`

Generate a new branch on an existing repository:

`npm run start -- --branch`

Generate a new branch with the same name and description as the last run:

`npm run start -- --branch --again`

## Examples

Articles and tutorials:

- [Building a Chrome Extension from Scratch using GitWit](https://codesphere.com/articles/building-a-chrome-extension-using-gitwit)

Examples of entire repositories generated with GitWit:

- [gitwitapp/doodle-app](https://github.com/gitwitapp/doodle-app): HTML/JS drawing app.
- [gitwitapp/cached-http-proxy-server](https://github.com/gitwitapp/cached-http-proxy-server): NodeJS proxy server with caching.
- [gitwitapp/reddit-news-viewer](https://github.com/gitwitapp/reddit-news-viewer): Python script for scraping Reddit headlines.
- [gitwitapp/python-discord-chatbot](https://github.com/gitwitapp/python-discord-chatbot): Simple Discord bot written in Python.
- [gitwitapp/live-BTC-ticker](https://github.com/gitwitapp/live-BTC-ticker): ReactJS app using d3.js to chart BTC prices.
- [gitwitapp/web-calculator](https://github.com/gitwitapp/web-calculator): Simple HTML/JS calculator.
- [gitwitapp/customer-oop-demo](https://github.com/gitwitapp/customer-oop-demo): Example of generated unit tests.

## Demos

The agent has two modes:
- Create new **repository**: Given a prompt and a repository name, spawn the repository
  
  https://github.com/gitwitdev/gitwitdev.github.io/assets/33395784/55537249-c301-4e13-84e5-0cdb06174071

- Create new **branch**: Given a prompt, an existing repository and a branch name, spawn the new branch

  https://github.com/gitwitdev/gitwitdev.github.io/assets/33395784/9315a17c-fc72-431a-a648-16ba42938faa

## Additional configuration

To add new repositories to a GitHub organization:
```sh
GITHUB_ORGNAME=mygithuborg
```

To use a remote Docker server:
```sh
DOCKER_API_HOST=1.2.3.4
DOCKER_API_PORT=2375
DOCKER_API_KEY=-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----
DOCKER_API_CA=-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----
DOCKER_API_CERT=-----BEGIN CERTIFICATE-----\n...\n-----END CERTIFICATE-----
```

To enable logging or caching with Helicone:
```sh
# Required:
OPENAI_BASE_URL=https://oai.hconeai.com/v1
HELICONE_API_KEY=sk-xxxxxxx-xxxxxxx-xxxxxxx-xxxxxxx
# Optional:
# OPENAI_CACHE_ENABLED=true
```

## LLM configuration

By default, GitWit Agent is set to use the OpenAI API with gpt-3.5-turbo and a temperature setting of 0.2. These settings can be configured in [index.js](https://github.com/jamesmurdza/gitwit-agent/blob/main/index.ts).

GitWit can also be used with LangChain to compose any LangChain supported [chat model](https://js.langchain.com/docs/modules/model_io/models/chat/). An example of this is in [llm.js](https://github.com/jamesmurdza/gitwit-agent/blob/langchain/llm.ts) on the [langchain](https://github.com/jamesmurdza/gitwit-agent/tree/langchain) branch.

## How it works

<table>
  <tr>
    <td valign="top" width="50%">
      <p>
        This shows the various APIs and connections in the program.
      </p>
      <p>
        <strong>Code generator</strong>: <a href="index.ts">(index.ts)</a> This is the central component that contains the logic necessary to create a new repository or branch.
      </p>
      <p>
        <strong>OpenAI API</strong>: <a href="openai.ts">(openai.ts)</a> This is a wrapper functions I wrote around the OpenAI chat completion API.
      </p>
      <p>
        <strong>GitHub API</strong>: <a href="github.ts">(github.ts)</a> This is a collection of wrapper functions I wrote around the GitHub API.
      </p>
      <p>
        <strong>Docker/Container</strong>: <a href="container.ts">(container.ts)</a> This is a collection of wrapper functions I wrote around dockerode to simplify interacting with a Docker server
      </p>
      <p>
        <strong>Git Repository</strong>: <a href="scripts.ts">(scripts.ts)</a> This is a collection of shell scripts that are injected into the container in order to perform basic git operations.
      </p>
    </td>
    <td>
      <img src="https://gitwitdev.github.io/images/gitwit-agent-architecture.png" alt="GitWit Architecture" width="100%" />
<p><em>Overview of the system and its parts</em></p>
    </td>
  </tr>
  <tr>
    <td valign="top">
      <p>
        This diagram shows a sequential breakdown of the steps in <a href="index.ts">index.ts</a>. A user prompt is used to generate a plan, which is then used to generate a shell script which is run in the container.
      </p>
      <p>
        Note: This diagram is for the "branch creation" mode. The equivalent diagram for "repository generation" mode would have "Create a new repo" for Step 1, and Step 2 would be removed. That's because the main purpose of the plan is to selectively decide which files to inject in the context of the final LLM call.
      </p>
      <p>
        We select entire files that should or should not be included in the context of the final LLM call, a simple implementation of retrieval-augmented generation!
      </p>
    </td>
    <td>
      <img src="https://gitwitdev.github.io/images/gitwit-agent-algorithm.png" alt="GitWit Agent" width="100%" />
<p><em>Overview of the agentic process</em></p>
    </td>
  </tr>
</table>
