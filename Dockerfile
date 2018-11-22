FROM ubuntu:18.04
RUN apt update && \
    apt install -y python3 python3-pip locales curl git&& \
    apt clean

RUN curl -sL https://deb.nodesource.com/setup_11.x | bash - && \
    apt install -y nodejs

# FIXME: Potential security issues
# FIXME: DO NOT RUN THINGS AS ROOT

RUN sed -i -e 's/# en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=en_US.UTF-8
ENV LANG en_US.UTF-8 
ENV LC_ALL en_US.UTF-8

RUN ln -sf /usr/bin/python3 /usr/bin/python && \
    ln -sf /usr/bin/pip3 /usr/bin/pip 

RUN git clone https://github.com/suminb/koob.git /app
WORKDIR /app
RUN pip install -r requirements.txt && \
    pip install -r tests/requirements.txt && \
    pip install -e .

CMD koob create-db && koob run