FROM node:16-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . ./
RUN npm run build
FROM node:16-alpine
WORKDIR /app
COPY --from=build /app/build /app
RUN npm install -g serve
CMD ["serve", "-s", ".", "-l", "3000"]
EXPOSE 3000
