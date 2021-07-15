FROM adoptopenjdk/openjdk15
VOLUME /tmp
ENV SERVER_TYPE=default
ENV SERVER_PORT=8081
ENV MAX_QUESTIONS=10
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["sh", "-c", "java -cp app:app/lib/* -Dspring.profiles.active=${SERVER_TYPE} -Dfile.encoding=UTF-8 -DmaxQuestions=${MAX_QUESTIONS} de.tkayser.quiz.QuizApplication --server.port=${SERVER_PORT}"]
