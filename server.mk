setup:
	curl -sS https://webinstall.dev/watchexec | bash
	go install github.com/pressly/goose/v3/cmd/goose@latest
	@echo $(INFO) "Make sure to  run update db before starting project..."

lint:
	./gradlew task spotlessApply

test:
	./gradlew check

functional-test:
	make start
