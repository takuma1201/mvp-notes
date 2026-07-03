# MVP Notes

Android CLI + Cursor で構築した、1画面構成のローカルメモ MVP アプリです。  
入力したテキストをリスト管理し、端末再起動後も DataStore に保存された内容を保持します。

## 主な機能

- **入力フォーム** — 新規項目または編集中のテキストを入力
- **リスト追加** — Add ボタンで項目を追加（空文字は不可）
- **リスト表示** — Material 3 の Card で一覧表示
- **編集** — Edit で入力欄に読み込み、Save で更新
- **削除** — 項目ごとに Delete
- **DataStore 永続化** — 追加・更新・削除を Preferences DataStore に保存

## 技術構成

| カテゴリ | 内容 |
|---------|------|
| 言語 | Kotlin |
| UI | Jetpack Compose / Material 3 |
| 状態管理 | ViewModel / StateFlow |
| 永続化 | DataStore Preferences（JSON シリアライズ） |
| データ層 | Repository Pattern |
| ビルド | Gradle |
| 開発環境 | Android CLI |

## アーキテクチャ

```
MainActivity
  └─ MainScreen
       └─ MainScreenViewModel
            └─ ItemRepository
                 └─ DataStoreItemRepository → DataStore
```

- **MainActivity** — テーマ設定と画面起動
- **MainScreen** — Compose UI（表示のみ、イベントは ViewModel へ委譲）
- **MainScreenViewModel** — UI 状態と CRUD ロジック
- **ItemRepository** — データ操作の抽象化
- **DataStore** — リストの永続化

## 開発コマンド

### 日常開発（Debug ビルド・インストール・起動）

```bash
./run-android.sh
```

### テスト + Debug ビルド

```bash
./gradlew testDebugUnitTest assembleDebug
```

### Release ビルド（未署名 APK）

```bash
./gradlew assembleRelease
```

### Release 版のローカル端末検証（debug 署名）

```bash
./run-release-local.sh
```

> `run-release-local.sh` は内部で `./gradlew assembleRelease -PlocalReleaseSign=true` を実行します。

## APK 出力先

| 種別 | パス |
|------|------|
| Debug | `app/build/outputs/apk/debug/app-debug.apk` |
| Release（未署名） | `app/build/outputs/apk/release/app-release-unsigned.apk` |
| Release（debug 署名・ローカル検証用） | `app/build/outputs/apk/release/app-release.apk` |

## 注意事項

- **`app-release-unsigned.apk`** は未署名のため、そのままでは端末にインストールできません。ビルド成果物の確認用です。
- **`app-release.apk`** は debug キーで署名したローカル検証用です。**Google Play への公開には使用しないでください。**
- **Play 公開** には release keystore を用意し、`app/build.gradle.kts` に本番用 `signingConfigs` を設定する必要があります。
- パッケージ名は `com.example.clisampleapp` です（MVP 段階のため変更していません）。

## プロジェクト情報

| 項目 | 値 |
|------|-----|
| アプリ名 | MVP Notes |
| applicationId | `com.example.clisampleapp` |
| minSdk | 24 |
| targetSdk | 36 |
| versionName | 1.0 |
