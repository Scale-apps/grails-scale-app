setup:
	@go install github.com/pressly/goose/v3/cmd/goose@latest
	@echo $(INFO) "Make sure to  run update db before starting project..."
	./gradlew build

start:
	./gradlew run	

lint:
	./gradlew :spotlessApply

check:
	./gradlew check

functional-test:
	make start
